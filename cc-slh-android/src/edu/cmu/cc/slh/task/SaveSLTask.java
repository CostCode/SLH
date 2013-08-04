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
 *  Saves the given ShoppingList object in the server as well as in the 
 *  local DB. 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 19, 2013
 */
public class SaveSLTask extends AsyncTask<ShoppingList, Void, Void> {

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
	
	public SaveSLTask(Context ctx, IAsyncActivity caller) {
		super();
		
		this.ctx = ctx;
		this.caller = caller;
	}

	//-------------------------------------------------------------------------
	// AsyncTask METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		if (!DeviceUtils.isNetworkConnectedElseAlert(
				ctx, getClass(), R.string.ws_error_noconnection)) {
			this.cancel(true);
		}
		
		caller.showProgressDialog(R.string.sl_all_saving);
		
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
			// Saving ShoppingList
			//---------------------------------------------------
			
			SoapObject response = saveServerSL(sl);
			
			int serverMemberVersion = parseMemberVersion(response);
			int localMemberVersion = retrieveLocalMemberVersion();
			
			if (serverMemberVersion == localMemberVersion + 1) {
				parseAndSaveSL(response, sl);
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
	 * Calls the web service to save the given ShoppingList object on 
	 * the server side.
	 * 
	 * @param sl - ShoppingList object to be saved
	 * @return soap web service method call response
	 * @throws Throwable - web service call exceptions
	 */
	private SoapObject saveServerSL(ShoppingList sl) throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				String.format("Saving ShoppingList[%s] on the server...", sl));
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_sl_namespace),
				ctx.getString(R.string.ws_sl_url));
		
		Map<String, String> arguments = 
				SecureWSHelper.initWSArguments(ctx, memberId);
		arguments.put(
				ctx.getString(R.string.ws_property_id), 
				String.valueOf(sl.getId()));
		arguments.put(
				ctx.getString(R.string.ws_sl_property_name), sl.getName());
		arguments.put(
				ctx.getString(R.string.ws_sl_property_date), 
				String.valueOf(sl.getDate().getTime()));
		arguments.put(
				ctx.getString(R.string.ws_sl_property_desc), 
				sl.getDescription());
		
		return service.invokeMethod(
				ctx.getString(R.string.ws_sl_method_saveSL), arguments);
	}
	
	/**
	 * Parses Member version number from the soap response
	 * 
	 * @param root - soap response
	 * @return server member version
	 * @throws Throwable - web service call exceptions
	 */
	private int parseMemberVersion(SoapObject root) throws Throwable {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Gets the local member version number
	 * 
	 * @return - local member version
	 */
	private int retrieveLocalMemberVersion() {
		return SLAdapter.retrieveMemberVersion();
	}
	
	/**
	 * Saves updated local member version
	 * 
	 * @param version - updated member version
	 */
	private void saveLocalMemberVersion(int version) {
		SLAdapter.persistMemberVersion(version);
	}
	
	/**
	 * Parses ShoppingList object save web service method call
	 * response to get the assigned ID number of the ShoppingList.
	 * Local ShoppingList object is then assigned to that id number 
	 * and saved. 
	 * 
	 * @param root - soap response
	 * @param sl - local ShoppingList object
	 */
	private void parseAndSaveSL(SoapObject root, ShoppingList sl) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		long id = SoapUtils.getLongPropertyValue(result, 
				ctx.getString(R.string.ws_property_id));
		sl.setId(id);
		
		saveSL(sl);
	}
	
	/**
	 * Saves the given ShoppingList locally.
	 * 
	 * @param sl - ShoppingList object
	 */
	private void saveSL(ShoppingList sl) {
		slDAO.save(sl);
	}

}
