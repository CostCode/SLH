/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import org.ksoap2.serialization.SoapObject;

import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.service.soap.util.SoapUtils;
import edu.cmu.cc.android.util.DeviceUtils;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.R;
import android.content.Context;
import android.os.AsyncTask;

/**
 *	This task is used to check the validity of the passed user membership id.
 *	
 *  @author Azamat Samiyev
 *	@version 2.0
 *  Date: Jun 13, 2013
 */
public class ActivationTask extends AsyncTask<String, Void, Boolean> {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private IActivationTaskCaller caller;
	
	private String memberId;
	
	private boolean errorState;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	public ActivationTask(Context ctx, IActivationTaskCaller caller) {
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
		
		//---------------------------------------------------
		// Checking whether the network is connected or not.
		// If not, we cancel the execution of the task.
		//---------------------------------------------------
		if (!DeviceUtils.isNetworkConnectedElseAlert(
				ctx, getClass(), R.string.ws_error_noconnection)) {
			this.cancel(true);
		}
		
		caller.showProgressDialog(R.string.activation_progressDialogTitle, 
				R.string.activation_progressDialogText);
		
		errorState = false;
		memberId = null;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		
		try {
			
			if (params == null || params[0] == null) {
				throw new IllegalArgumentException("Invalid input parameter: " +
						"MembershipID is null");
			}
			
			memberId = params[0];
			
			SoapObject response = validateMembership(memberId);
			
			return parseValidity(response);
			
		} catch (Throwable t) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), t);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		caller.dismissProgressDialog();
		if (!errorState) {
			caller.onActivationTaskSucceeded(memberId, result);
		}
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Performs WebService call to check the validity of the membership id
	 * 
	 * @param memberId - membership id
	 * @return Soap response
	 * @throws Throwable - web service call exceptions
	 */
	private SoapObject validateMembership(final String memberId) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				String.format("Validating MembershipID[%s] on the server...", 
						memberId));
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_member_namespace), 
				ctx.getString(R.string.ws_member_url));
		
		return service.invokeMethod(
				ctx.getString(R.string.ws_member_method_validate), 
				SecureWSHelper.initWSArguments(ctx, memberId));
	}
	
	/**
	 * Parses membership status validity response
	 * 
	 * @param root - SoapObject response
	 * @return true - valid member id, false - invalid
	 */
	private boolean parseValidity(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		return SoapUtils.getBooleanPropertyValue(result, 
				ctx.getString(R.string.ws_member_property_validity));
	}
	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------

	/**
	 * The purpose of this Interface is to enable the task to return its
	 * result to the caller activity.
	 */
	public interface IActivationTaskCaller extends IAsyncActivity {
		
		/**
		 * Method is called to inform the caller that activation task has
		 * completed and its result is passed.
		 * 
		 * @param memberId - membership id of the user
		 * @param activated - activation status. true - activated, false - not
		 */
		public void onActivationTaskSucceeded(String memberId, 
				boolean activated);
		
	}
	

}
