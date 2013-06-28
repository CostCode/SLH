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
 *  Date: Jun 21, 2013
 */
public class ShoppingListAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static String KEY_SHOPPINGLISTS_VERSION = "shoppinglists-version";

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
	
	public static synchronized boolean persistVersion(int version) {
		
		return saveToSharedPrefs(ApplicationState.getContext(), 
				KEY_SHOPPINGLISTS_VERSION, String.valueOf(version), 
				R.string.shoppinglist_all_error_versionPersist);
	}
	
	public static int retrieveVersion() {
		
		String strValue = retrieveFromSharedPrefs(ApplicationState.getContext(), 
				KEY_SHOPPINGLISTS_VERSION, 
				R.string.shoppinglist_all_error_versionRetrieve);
		
		return Integer.parseInt(strValue);
	}
	
	

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private static boolean saveToSharedPrefs(Context ctx, 
			String key, String value, int errMsgResID) {
		
		try {
			
			return SharedPrefsAdapter.persist(ctx, key, value);
			
		} catch (Throwable t) {
			String errMsg = getErrorMessage(ctx, errMsgResID, t);
			Logger.logErrorAndAlert(ctx, ShoppingListAdapter.class, errMsg, t);
		}
		
		return false;
	}
	
	private static String retrieveFromSharedPrefs(Context ctx, String key, 
			int errMsgResID) {
		
		try {
			
			return SharedPrefsAdapter.retrieve(ctx, key);
			
		} catch (Throwable t) {
			String errMsg = getErrorMessage(ctx, errMsgResID, t);
			Logger.logErrorAndAlert(ctx, ShoppingListAdapter.class, errMsg, t);
		}
		
		return null;
	}
	
	private static String getErrorMessage(Context ctx, 
			int errMsgResID, Throwable t) {
		
		return StringUtils.getLimitedString(
				ctx.getString(errMsgResID, t.getMessage()), 200, "...");
	}

}
