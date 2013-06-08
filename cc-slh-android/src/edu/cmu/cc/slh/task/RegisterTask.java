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
 *  DESCRIPTION: This task is used to validate Costco membership id
 *  of the user to be created.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 7, 2013
 */
public class RegisterTask extends AsyncTask<String, Void, Boolean> {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** Log tag - class identification */
	private static final String TAG = RegisterTask.class.getName();

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
	public RegisterTask() {
		
		service = new WebService();
		
	}
	
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------
	
	
	/**
	 * Validate new user membership id
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		
		String membershipId = params[0];
		
		Log.d(TAG, String.format("Validating user membership id [MEMBERSHIP ID: %s]", 
				membershipId));
		
		Map<String, String> arguments = new HashMap<String, String>(1);
		arguments.put(SOAPContract.User.MEMBER_ID, membershipId);
		
		Boolean validity = null;
		
		try {
			
			SoapObject result = service.invokeMethod(
					SOAPContract.Methods.VALIDATE_MEMBERSHIP, arguments);
			
			validity = (Boolean) result.getProperty(
					SOAPContract.User.MEMBERSHIP_VALIDITY);
			
			Log.d(TAG, String.format("Membership validation result: %s", validity));
			
		} catch (Exception e) {
			Log.e(TAG, "Web Service communication error...", e);
		}
		
		return validity;
		
	}//doInBackground

}
