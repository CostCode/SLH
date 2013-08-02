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
 *  This task is used for downloading the list of all Costco Warehouses.
 *  There is no UI activity which executes this task. 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class FetchWarehousesTask extends AsyncTask<Void, Void, Void> {

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
	// AsyncTask METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		//---------------------------------------------------
		// Checking whether the network is connected or not.
		// If not, we cancel the execution of the task.
		//---------------------------------------------------
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
				throw new IllegalArgumentException("MemberID is null " +
						"or empty!");
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
	
	/**
	 * Gets client warehouses version number
	 * 
	 * @return warehouses version number
	 */
	private int retrieveLocalWarehousesVersion() {
		return WarehouseAdapter.retrieveVersion();
	}
	
	/**
	 * Gets the current version of the warehouses table from the server.
	 * 
	 * @return server warehouses version
	 * @throws Throwable - web service call exceptions
	 */
	private int retrieveServerWarehousesVersion() throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving Warehouses Version number from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		SoapObject response = service.invokeMethod(ctx.getString(
				R.string.ws_warehouse_method_retrieveWarehousesVersion), 
				SecureWSHelper.initWSArguments(ctx, memberId));
		
		return parseWarehousesVersion(response);
	}
	
	/**
	 * Parses warehouses version number from the SoapObject
	 * 
	 * @param root - SoapObject
	 * @return server warehouses version number
	 */
	private int parseWarehousesVersion(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		if (SoapUtils.hasException(ctx, result)) {
			throw new IllegalStateException(
					SoapUtils.getException(ctx, result));
		}
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Deletes all the local Warehouse objects from the local DB
	 */
	private void deleteLocalWarehouses() {
		warehouseDAO.deleteAll();
	}
	
	/**
	 * Calls the web service to get the list of Warehouses and
	 * saves them in the local DB.
	 * 
	 * @throws Throwable - web service call exceptions
	 */
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
	
	/**
	 * Parses Soap response and creates Warehouse objects based on that.
	 * Also, saves each warehouse object in the local DB.
	 * 
	 * @param root - SoapObject
	 */
	private void parseAndSaveWarehousesXML(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		if (SoapUtils.hasException(ctx, result)) {
			throw new IllegalStateException(
					SoapUtils.getException(ctx, result));
		}
		
		for (int i = 0; i < result.getPropertyCount(); i++) {
			
			SoapObject warehouseProperty = 
					(SoapObject) result.getProperty(i);
			
			Warehouse wh = new Warehouse();
			wh.setId(SoapUtils.getLongPropertyValue(warehouseProperty, 
					ctx.getString(R.string.ws_warehouse_property_id)));
			wh.setAddress(warehouseProperty.getPropertyAsString(
					R.string.ws_warehouse_property_address));
			wh.setVersion(SoapUtils.getIntPropertyValue(warehouseProperty, 
					ctx.getString(R.string.ws_property_version)));
			
			saveWarehouse(wh);
		}
		
	}
	
	/**
	 * Saves the given Warehouse object in the local DB
	 * 
	 * @param wh - Warehouse object
	 */
	private void saveWarehouse(Warehouse wh) {
		warehouseDAO.save(wh);
	}
	
	/**
	 * Saves the updated warehouses version as the local warehouses version 
	 * 
	 * @param version - warehouses version
	 */
	private void saveLocalWarehousesVersion(int version) {
		WarehouseAdapter.persistVersion(version);
	}
	
	/**
	 * Loads all the warehouses from the local DB and stores them
	 * in the central application state
	 */
	private void loadLocalWarehouses() {
		List<Warehouse> warehouses = warehouseDAO.getAll();
		ApplicationState.getInstance().setWarehouses(warehouses);
	}

}
