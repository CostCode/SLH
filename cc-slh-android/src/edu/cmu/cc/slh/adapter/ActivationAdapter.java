/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import android.content.Context;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.SharedPrefsAdapter;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 19, 2013
 */
public class ActivationAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static String KEY_ACTIVATION_STATUS = "activation-status";

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistActivationStatus(boolean status) {
		
		return saveToSharedPrefs(ApplicationState.getContext(), 
				KEY_ACTIVATION_STATUS, String.valueOf(status));
	}
	
	public static boolean retrieveActivationStatus() {
		
		return retrieveFromSharedPrefs(ApplicationState.getContext(), 
				KEY_ACTIVATION_STATUS);
	}

	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------
	
	private static boolean saveToSharedPrefs(Context ctx, 
			String key, String value) {
		
		try {
			
			return SharedPrefsAdapter.persist(ctx, key, value);
			
		} catch (Throwable t) {
			
			String errMsg = getErrorMessage(
					ctx, R.string.activation_error_status_persist, t);
			
			Logger.logErrorAndAlert(ctx, ActivationAdapter.class, errMsg, t);
		}
		
		return false;
	}
	
	private static boolean retrieveFromSharedPrefs(Context ctx, String key) {
		
		try {
			
			String strValue = SharedPrefsAdapter.retrieve(ctx, key);
			
			if (!StringUtils.isNullOrEmpty(strValue)) {
				return Boolean.valueOf(strValue);
			}
			
		} catch (Throwable t) {
			
			String errMsg = getErrorMessage(
					ctx, R.string.activation_error_status_retrieve, t);
			
			Logger.logErrorAndAlert(ctx, ActivationAdapter.class, errMsg, t);
		}
		
		return false;
	}
	
	private static String getErrorMessage(Context ctx, 
			int errMessageResID, Throwable t) {
		
		return StringUtils.getLimitedString(
				ctx.getString(errMessageResID, t.getMessage()), 200, "...");
	}

}
