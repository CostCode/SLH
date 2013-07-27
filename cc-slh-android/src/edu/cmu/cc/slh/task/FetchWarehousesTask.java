/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.List;

import org.ksoap2.serialization.SoapObject;

import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.service.soap.util.SoapUtils;
import edu.cmu.cc.android.util.DeviceUtils;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActivationAdapter;
import edu.cmu.cc.slh.adapter.WarehouseAdapter;
import edu.cmu.cc.slh.dao.WarehouseDAO;
import edu.cmu.cc.slh.model.Warehouse;
import android.content.Context;
import android.os.AsyncTask;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class FetchWarehousesTask extends AsyncTask<Void, Void, Void> {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private WarehouseDAO warehouseDAO;
	
	private String memberId;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public FetchWarehousesTask(Context ctx) {
		super();
		
		this.ctx = ctx;
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		if (!DeviceUtils.isNetworkConnectedElseAlert(
				ctx, getClass(), R.string.ws_error_noconnection)) {
			this.cancel(true);
		}
		
		warehouseDAO = new WarehouseDAO();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			
			//---------------------------------------------------
			// Getting User Membership ID
			//---------------------------------------------------
			
			memberId = ActivationAdapter.retrieveMemberId();
			if (StringUtils.isNullOrEmpty(memberId)) {
				throw new RuntimeException("MemberID is null or empty!");
			}
			
			//---------------------------------------------------
			// Warehouse version checking and fetching
			//---------------------------------------------------
			
			int localWarehousesVersion = retrieveLocalWarehousesVersion();
			int serverWarehousesVersion = retrieveServerWarehousesVersion();
			
			if (localWarehousesVersion != serverWarehousesVersion) {
				deleteLocalWarehouses();
				retrieveAndSaveServerWarehouses();
				saveLocalWarehousesVersion(serverWarehousesVersion);
			}
			
			loadLocalWarehouses();
		} catch (Throwable t) {
			Logger.logErrorAndThrow(getClass(), t);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		warehouseDAO.close();
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private int retrieveLocalWarehousesVersion() {
		return WarehouseAdapter.retrieveVersion();
	}
	
	private int retrieveServerWarehousesVersion() throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving Warehouses Version number from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_warehouse_method_retrieveWarehousesVersion), 
				SecureWSHelper.initWSArguments(ctx, memberId));
		
		String strVersion = response.getPropertyAsString(ctx
				.getString(R.string.ws_warehouse_property_version));
		
		Logger.logDebug(this.getClass(), 
				String.format("Server Warehouses Version: [%s]", strVersion));
		
		return Integer.parseInt(strVersion);
	}
	
	private void deleteLocalWarehouses() {
		warehouseDAO.deleteAll();
	}
	
	private void retrieveAndSaveServerWarehouses() throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving Warehouses from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_warehouse_method_retrieveWarehouses), 
				SecureWSHelper.initWSArguments(ctx, memberId));

		parseAndSaveWarehousesXML(response);
	}
	
	private void parseAndSaveWarehousesXML(SoapObject root) {
		
		if (root == null) {
			throw new RuntimeException("Soap source is null");
		}
		
		Object warehousesProperty = root.getProperty(
				ctx.getString(R.string.ws_warehouse_property_warehouses));
		
		if (warehousesProperty instanceof SoapObject) {
			SoapObject warehousesXml = (SoapObject) warehousesProperty;
			
			for (int i = 0; i < warehousesXml.getPropertyCount(); i++) {
				Object warehouseProperty = warehousesXml.getProperty(i);
				if (warehouseProperty instanceof SoapObject) {
					SoapObject warehouseXml = (SoapObject) warehouseProperty;
					
					Warehouse wh = new Warehouse();
					wh.setId(SoapUtils.getLongPropertyValue(warehouseXml, 
							ctx.getString(R.string.ws_warehouse_property_id)));
					wh.setAddress(warehouseXml.getPropertyAsString(
							R.string.ws_warehouse_property_address));
					wh.setVersion(SoapUtils.getIntPropertyValue(warehouseXml, 
							ctx.getString(R.string.ws_warehouse_property_version)));
					
					saveWarehouse(wh);
				}
			}
		}
		
	}
	
	private void saveWarehouse(Warehouse wh) {
		warehouseDAO.save(wh);
	}
	
	private void saveLocalWarehousesVersion(int version) {
		WarehouseAdapter.persistVersion(version);
	}
	
	private void loadLocalWarehouses() {
		List<Warehouse> warehouses = warehouseDAO.getAll();
		ApplicationState.getInstance().setWarehouses(warehouses);
	}

}
