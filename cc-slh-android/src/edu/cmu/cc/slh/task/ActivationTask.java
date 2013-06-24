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
import edu.cmu.cc.slh.R;
import android.content.Context;
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
	
	private Context ctx;
	
	private IActivationTaskCaller caller;
	
	private boolean errorState;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	/**
	 * Constructor.
	 */
	public ActivationTask(Context ctx, IActivationTaskCaller caller) {
		super();
		
		this.ctx = ctx;
		this.caller = caller;
	}
	
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		caller.showProgressDialog(R.string.activation_progressDialogTitle, 
				R.string.activation_progressDialogText);
	}
	
	/**
	 * Validate new user membership id
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		
		errorState = false;
		
		
		SoapWebService service = new SoapWebService(ctx.getString(R.string.ws_url), 
				ctx.getString(R.string.ws_namespace));
		
		String membershipId = params[0];
		
		Logger.logDebug(this.getClass(), 
				String.format("Validating user membership id " +
				"[MEMBERSHIP ID: %s]", membershipId));
		
		Map<String, String> arguments = new HashMap<String, String>(1);
		arguments.put(ctx.getString(R.string.ws_activation_memberid), membershipId);
		
		try {
			
			SoapObject soapResponse = service.invokeMethod(
					ctx.getString(R.string.ws_activation_validate), arguments);
			
			String strResult = soapResponse.getProperty(
					ctx.getString(R.string.ws_activation_validity)).toString();
			
			Boolean validationResult = Boolean.valueOf(strResult);
			
			Logger.logDebug(this.getClass(), String.format("Membership " +
					"validation result: %s", validationResult));
			
			return validationResult;
			
		} catch (Exception e) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), e);
		}
		
		return null;
	}
	
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
