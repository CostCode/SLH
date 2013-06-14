/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.soap.SOAPContract;
import android.os.AsyncTask;

/**
 *  DESCRIPTION: This task activates this application is the provided
 *  Costco membership id is valid. 
 *	
 *  @author Azamat Samiyev
 *	@version 2.0
 *  Date: Jun 13, 2013
 */
public class ActivationTask extends AsyncTask<String, Void, Boolean> {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private IActivationTaskCaller caller;
	
	private boolean errorState;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	/**
	 * Constructor.
	 */
	public ActivationTask(IActivationTaskCaller caller) {
		super();
		
		this.caller = caller;
	}
	
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		caller.showProgressDialog();
	}
	
	/**
	 * Validate new user membership id
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		
		errorState = false;
		
		SoapWebService service = new SoapWebService(SOAPContract.URL, 
				SOAPContract.NAMESPACE);
		
		String membershipId = params[0];
		
		Logger.logDebug(this.getClass(), 
				String.format("Validating user membership id " +
				"[MEMBERSHIP ID: %s]", membershipId));
		
		Map<String, String> arguments = new HashMap<String, String>(1);
		arguments.put(SOAPContract.Membership.MEMBERSHIP_ID, membershipId);
		
		try {
			
			SoapObject result = service.invokeMethod(
					SOAPContract.Membership.METHOD_VALIDATE, arguments);
			
			String strResult = result.getProperty(
					SOAPContract.Membership.VALIDATION_RESULT).toString();
			
			Boolean validationResult = Boolean.valueOf(strResult);
			
			Logger.logDebug(this.getClass(), String.format("Membership " +
					"validation result: %s", validationResult));
			
			return validationResult;
			
		} catch (Exception e) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), e);
			return null;
		}
		
	}//doInBackground
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		caller.dismissProgressDialog();
		if (!errorState) {
			caller.onActivationTaskSucceeded(result);
		}
	}

	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------

	/**
	 * The purpose of this Interface is to enable the task to return its
	 * result to the caller.
	 */
	public interface IActivationTaskCaller extends IAsyncActivity {
		
		/**
		 * Activation request was successfully handled
		 * @param activated - activation result
		 */
		public void onActivationTaskSucceeded(boolean activated);
		
	}
	

}
