/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.os.AsyncTask;
import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.service.soap.util.SoapUtils;
import edu.cmu.cc.android.util.DeviceUtils;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActivationAdapter;
import edu.cmu.cc.slh.dao.SLDAO;
import edu.cmu.cc.slh.dao.SLItemDAO;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;

/**
 *  Tries to save the given ShoppingListItem object first in the server,
 *  then if it succeeds, saves it in the local DB.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 20, 2013
 */
public class SaveSLItemTask extends AsyncTask<ShoppingListItem, Void, Void> {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private IAsyncActivity caller;
	
	private SLDAO slDAO;
	
	private SLItemDAO itemDAO;
	
	private String memberId;
	
	private boolean errorState;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public SaveSLItemTask(Context ctx, IAsyncActivity caller) {
		super();
		
		this.ctx = ctx;
		this.caller = caller;
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
		
		caller.showProgressDialog(R.string.sl_item_saving);
		
		slDAO = new SLDAO();
		itemDAO = new SLItemDAO();
		
		errorState = false;
	}
	
	@Override
	protected Void doInBackground(ShoppingListItem... params) {
		
		try {
			
			if (params == null || params[0] == null) {
				throw new IllegalArgumentException("Invalid input parameter: " +
						"ShoppingListItem is null");
			}
			ShoppingListItem item = params[0];
			
			//---------------------------------------------------
			// Getting User Membership ID
			//---------------------------------------------------
			
			memberId = ActivationAdapter.retrieveMemberId();
			if (StringUtils.isNullOrEmpty(memberId)) {
				throw new IllegalArgumentException("MemberID is null or empty!");
			}
			
			//---------------------------------------------------
			// Saving ShoppingListItem
			//---------------------------------------------------
			
			SoapObject response = saveServerSLItem(item);
			
			int serverSLVersion = parseSLVersion(response);
			int localSLVersion = retrieveLocalSLVersion(item.getShoppingList());
			
			if (serverSLVersion == localSLVersion + 1) {
				parseAndSaveSLItem(response, item);
				saveLocalSLVersion(serverSLVersion, item.getShoppingList());
			}
			
		} catch (Throwable t) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), t);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		itemDAO.close();
		slDAO.close();
		
		caller.dismissProgressDialog();
		if (!errorState) {
			caller.onAsyncTaskSucceeded(getClass());
		}
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Calls the web service to save the given ShoppingListItem object.
	 * 
	 * @param item - ShoppingListItem object
	 * @return soap response
	 * @throws Throwable - web service call exceptions
	 */
	private SoapObject saveServerSLItem(ShoppingListItem item) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(), String.format("Saving " +
				"ShoppingListItem[%s] on the server...", item));
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_sl_namespace),
				ctx.getString(R.string.ws_sl_url));
		
		Map<String, String> arguments = 
				SecureWSHelper.initWSArguments(ctx, memberId);
		arguments.put(
				ctx.getString(R.string.ws_property_id),
				String.valueOf(item.getId()));
		arguments.put(
				ctx.getString(R.string.ws_sl_property_id), 
				String.valueOf(item.getShoppingList().getId()));
		arguments.put(
				ctx.getString(R.string.ws_sl_item_property_category), 
				String.valueOf(item.getCategory().getId()));
		arguments.put(
				ctx.getString(R.string.ws_sl_item_property_name), 
				item.getName());
		arguments.put(
				ctx.getString(R.string.ws_sl_item_property_quantity), 
				String.valueOf(item.getQuantity()));
		arguments.put(
				ctx.getString(R.string.ws_sl_item_property_price), 
				String.valueOf(item.getPrice().doubleValue()));
		arguments.put(
				ctx.getString(R.string.ws_sl_item_property_unit), 
				String.valueOf(item.getUnit()));
		arguments.put(
				ctx.getString(R.string.ws_sl_item_property_desc), 
				item.getDescription());
		
		return service.invokeMethod(
				ctx.getString(R.string.ws_sl_item_method_saveSLItem), arguments);
	}
	
	/**
	 * Parses ShoppingList version number from the Soap response
	 * 
	 * @param root - soap response
	 * @return ShoppingList version number
	 * @throws Throwable - web service call exceptions
	 */
	private int parseSLVersion(SoapObject root) throws Throwable {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Gets the local ShoppingList version number
	 * 
	 * @param sl - local ShoppingList
	 * @return local ShoppingList version
	 */
	private int retrieveLocalSLVersion(ShoppingList sl) {
		
		return sl.getVersion();
	}
	
	/**
	 * Parses the soap response to get the newly saved item id and saves 
	 * the local ShoppinglistItem with that id into the DB.
	 * 
	 * @param root - soap response
	 * @param item - ShoppingListItem object
	 */
	private void parseAndSaveSLItem(SoapObject root, ShoppingListItem item) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		long id = SoapUtils.getLongPropertyValue(result, 
				ctx.getString(R.string.ws_property_id));
		item.setId(id);
		
		saveSLItem(item);
	}
	
	/**
	 * Saves the given ShoppingListItem object into the local DB.
	 * 
	 * @param item - ShoppingListItem object
	 */
	private void saveSLItem(ShoppingListItem item) {
		itemDAO.save(item);
	}
	
	/**
	 * Updates the version number of the parent ShoppingList object
	 * 
	 * @param version - new version number
	 * @param parentSL - parent ShoppingList object
	 */
	private void saveLocalSLVersion(int version, ShoppingList parentSL) {
		parentSL.setVersion(version);
		slDAO.save(parentSL);
		ApplicationState.getInstance().setCurrentSL(parentSL);
	}
	

}
