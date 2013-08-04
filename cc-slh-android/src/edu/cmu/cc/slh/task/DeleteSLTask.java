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
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActivationAdapter;
import edu.cmu.cc.slh.adapter.SLAdapter;
import edu.cmu.cc.slh.dao.SLDAO;
import edu.cmu.cc.slh.model.ShoppingList;

/**
 *  Deletes the given ShoppingList object from the server as well as from
 *  the local DB.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 20, 2013
 */
public class DeleteSLTask extends AsyncTask<ShoppingList, Void, Void> {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private IAsyncActivity caller;
	
	private SLDAO slDAO;
	
	private String memberId;
	
	private boolean errorState;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public DeleteSLTask(Context ctx, IAsyncActivity caller) {
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
		
		caller.showProgressDialog(R.string.sl_all_deleting);
		
		slDAO = new SLDAO();
		
		errorState = false;
	}
	
	@Override
	protected Void doInBackground(ShoppingList... params) {
		
		try {
			
			if (params == null || params[0] == null) {
				throw new IllegalArgumentException("Invalid input parameter: " +
						"ShoppingList is null");
			}
			ShoppingList sl = params[0];
			
			//---------------------------------------------------
			// Getting User Membership ID
			//---------------------------------------------------
			
			memberId = ActivationAdapter.retrieveMemberId();
			if (StringUtils.isNullOrEmpty(memberId)) {
				throw new IllegalArgumentException("MemberID is null or empty!");
			}
			
			//---------------------------------------------------
			// Deleting ShoppingList
			//---------------------------------------------------
			
			SoapObject response = deleteServerSL(sl);
			
			int serverMemberVersion = parseMemberVersion(response);
			int localMemberVersion = retrieveLocalMemberVersion();
			
			if (serverMemberVersion == localMemberVersion + 1) {
				deleteLocalSL(sl);
				saveLocalMemberVersion(serverMemberVersion);
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
	 * Calls the web service to delete the given ShoppingList object
	 * from the server.
	 * 
	 * @param sl - ShoppingList object
	 * @return soap response
	 * @throws Throwable - web service call exceptions
	 */
	private SoapObject deleteServerSL(ShoppingList sl) throws Throwable {
		
		Logger.logDebug(this.getClass(), String.format("Deleting " +
				"ShoppingList[%s] from the server...", sl));
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_sl_namespace),
				ctx.getString(R.string.ws_sl_url));
		
		Map<String, String> arguments = 
				SecureWSHelper.initWSArguments(ctx, memberId);
		arguments.put(
				ctx.getString(R.string.ws_property_id), 
				String.valueOf(sl.getId()));

		return service.invokeMethod(
				ctx.getString(R.string.ws_sl_method_deleteSL), arguments);
	}
	
	/**
	 * Parses member version number from the soap response.
	 * 
	 * @param root - soap response
	 * @return member version
	 */
	private int parseMemberVersion(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Gets the local member version number
	 * 
	 * @return local member version number
	 */
	private int retrieveLocalMemberVersion() {
		return SLAdapter.retrieveMemberVersion();
	}
	
	/**
	 * Deletes the given ShoppingList object from the local
	 * DB.
	 * 
	 * @param sl - ShoppingList object
	 */
	private void deleteLocalSL(ShoppingList sl) {
		slDAO.delete(sl);
	}
	
	/**
	 * Updates the local member version number
	 * 
	 * @param version - updated member version number
	 */
	private void saveLocalMemberVersion(int version) {
		SLAdapter.persistMemberVersion(version);
	}

}
