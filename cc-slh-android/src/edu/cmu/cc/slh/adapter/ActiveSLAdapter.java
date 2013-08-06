/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import java.util.List;

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
	
	private static final String KEY_ACTIVE_SHOPPINGLIST = "active-shoppinglist";
	
	//-------------------------------------------------------------------------
	// ACTIVE SHOPPINGLIST
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistActiveSL(ShoppingList sl) {
		
		long id = 0;
		
		if (sl != null) {
			id = sl.getId();
		}
		
		return saveToSharedPrefs(ActiveSLAdapter.class, 
				ApplicationState.getContext(), KEY_ACTIVE_SHOPPINGLIST, 
				Long.valueOf(id), R.string.sl_active_error_persist);
	}
	
	public static ShoppingList retrieveActiveSL() {
		
		long id = (Long) retrieveFromSharedPrefs(ActiveSLAdapter.class, 
				ApplicationState.getContext(), KEY_ACTIVE_SHOPPINGLIST,
				Long.class, R.string.sl_active_error_retrieve);
		
		return findSLById(id);
	}
	
	public static synchronized void clearActiveSL(ShoppingList sl) {
		
		ShoppingList activeSL = retrieveActiveSL();
		if (activeSL != null) {
			if (activeSL.equals(sl)) {
				persistActiveSL(null);
			}
		}
	}
	

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private static ShoppingList findSLById(long id) {
		
		if (id <= 0) {
			return null;
		}
		
		List<ShoppingList> list = 
				ApplicationState.getInstance().getShoppingLists();
		
		if (list != null && list.size() > 0) {
			for (ShoppingList sl : list) {
				if (sl.getId() == id) {
					return sl;
				}
			}
		}
		
		return null;
	}

}
