/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import java.util.List;

import android.content.Context;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.SharedPrefsAdapter;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.ShoppingList;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
public class ActiveSLAdapter extends AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static String KEY_ACTIVE_SHOPPINGLIST = "active-shoppinglist";
	
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
	
	public static synchronized boolean persistActiveSL(ShoppingList sl) {
		
		return saveToSharedPrefs(ApplicationState.getContext(), 
				KEY_ACTIVE_SHOPPINGLIST, String.valueOf(sl.getId()), 
				R.string.sl_active_error_persist);
	}
	
	public static ShoppingList retrieveActiveShoppingList() {
		
		String strId = retrieveFromSharedPrefs(ApplicationState.getContext(), 
				KEY_ACTIVE_SHOPPINGLIST, 
				R.string.sl_active_error_retrieve);
		
		long id = Long.parseLong(strId);
		
		List<ShoppingList> list = 
				ApplicationState.getInstance().getShoppingLists();
		
		if (list != null && list.size() > 0) {
			for (ShoppingList sl : list) {
				if (sl.getId() == id) {
					return sl;
				}
			}
			
			return list.get(0);
		}
		
		return null;
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
			Logger.logErrorAndAlert(ctx, SLAdapter.class, errMsg, t);
		}
		
		return false;
	}
	
	private static String retrieveFromSharedPrefs(Context ctx, String key, 
			int errMsgResID) {
		
		try {
			
			return SharedPrefsAdapter.retrieve(ctx, key);
			
		} catch (Throwable t) {
			String errMsg = getErrorMessage(ctx, errMsgResID, t);
			Logger.logErrorAndAlert(ctx, SLAdapter.class, errMsg, t);
		}
		
		return null;
	}

}
