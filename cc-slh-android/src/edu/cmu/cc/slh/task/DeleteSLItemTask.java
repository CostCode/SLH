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
 *  Deletes the given ShoppingListItem object from the server and local DB. 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 22, 2013
 */
public class DeleteSLItemTask extends AsyncTask<ShoppingListItem, Void, Void> {

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
	
	public DeleteSLItemTask(Context ctx, IAsyncActivity caller) {
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
		
		caller.showProgressDialog(R.string.sl_item_deleting);
		
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
			// Deleting ShoppingListItem
			//---------------------------------------------------
			
			SoapObject response = deleteServerSLItem(item);
			
			int serverSLVersion = parseSLVersion(response);
			int localSLVersion = retrieveLocalSLVersion(item.getShoppingList());
			
			if (serverSLVersion == localSLVersion + 1) {
				deleteLocalSLItem(item);
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
	 * Calls the web service to delete the given ShoppingListItem object.
	 * 
	 * @param item - ShoppingListItem object
	 * @return soap response
	 * @throws Throwable - web service call exceptions
	 */
	private SoapObject deleteServerSLItem(ShoppingListItem item) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(), String.format("Deleting " +
				"ShoppingListItem[%s] from the server...", item));
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_sl_namespace),
				ctx.getString(R.string.ws_sl_url));
		
		Map<String, String> arguments = 
				SecureWSHelper.initWSArguments(ctx, memberId);
		arguments.put(
				ctx.getString(R.string.ws_sl_property_id), 
				String.valueOf(item.getShoppingList().getId()));
		arguments.put(
				ctx.getString(R.string.ws_sl_item_property_id), 
				String.valueOf(item.getId()));
		
		return service.invokeMethod(
				ctx.getString(R.string.ws_sl_item_method_deleteSLItem), 
				arguments);
	}
	
	/**
	 * Parses the updated ShoppingList version number from
	 * the soap response
	 * 
	 * @param root - soap response
	 * @return ShoppingList version
	 */
	private int parseSLVersion(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Gets the old version number of the ShoppingList
	 * 
	 * @param sl - ShoppingList
	 * @return old version number
	 */
	private int retrieveLocalSLVersion(ShoppingList sl) {
		return sl.getVersion();
	}
	
	/**
	 * Deletes the given ShoppingListItem object from the local DB.
	 * 
	 * @param item - ShoppingListItem
	 */
	private void deleteLocalSLItem(ShoppingListItem item) {
		itemDAO.delete(item);
	}
	
	/**
	 * Updates the version number of the parent ShoppingList.
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
