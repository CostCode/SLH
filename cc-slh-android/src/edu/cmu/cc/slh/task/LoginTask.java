/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import edu.cmu.cc.android.soap.WebService;
import edu.cmu.cc.slh.soap.SOAPContract;
import android.os.AsyncTask;
import android.util.Log;

/**
 *  DESCRIPTION: User authorization task. It communicates
 *  with the web service to validate the user login data.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 5, 2013
 */
public class LoginTask extends AsyncTask<String, Void, Boolean> {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** Log tag - class identification */
	private static final String TAG = LoginTask.class.getName();

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	/** SOAP Web service manager */
	private WebService service;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	/**
	 * Constructor.
	 */
	public LoginTask() {
		
		service = new WebService();
		
	}
	
	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Parse SOAP object to get the result of user authorization.
	 * @param result
	 * @return
	 */
	private Boolean parseResult(SoapObject result) {
		
		Boolean authorized = null;
		try {
			authorized = 
					(Boolean) result.getProperty(SOAPContract.User.AUTHORIZED);
		} catch (Exception e) {
			Log.e(TAG, String.format("SoapObject does not have property: [%s]", 
					SOAPContract.User.AUTHORIZED), e);
		}
		
		return authorized;
		
	}//parseResult
	
	/**
	 * Authorize the user.
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		
		String username = params[0];
		String password = params[1];
		
		Log.d(TAG, String.format("Authorizing the user [USERNAME: %s, PASSWORD: %s]", 
				username, password));
		
		Map<String, String> arguments = new HashMap<String, String>(2);
		arguments.put(SOAPContract.User.USERNAME, username);
		arguments.put(SOAPContract.User.PASSWORD, password);
		
		SoapObject result = null;
		try {
			
			result = service
					.invokeMethod(SOAPContract.Methods.AUTHORIZE_USER, arguments);
			
		} catch (Exception e) {
			Log.e(TAG, "User authorization exception...", e);
		}
		
		return parseResult(result);
		
	}//doInBackground


	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		if (result != null) {
			
		}
		
	}//onPostExecute
	
	

}
