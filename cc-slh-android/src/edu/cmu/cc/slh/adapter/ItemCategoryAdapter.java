/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 18, 2013
 */
public class ItemCategoryAdapter extends AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String KEY_ITEMCATEGORIES_VERSION = "version-itemcategories";

	//-------------------------------------------------------------------------
	// CATEGORIES VERSION
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistVersion(int version) {
		
		return saveToSharedPrefs(ItemCategoryAdapter.class, 
				ApplicationState.getContext(), KEY_ITEMCATEGORIES_VERSION, 
				Integer.valueOf(version), R.string.itemcategory_error_versionPersist);
	}
	
	public static int retrieveVersion() {
		
		return (Integer) retrieveFromSharedPrefs(ItemCategoryAdapter.class, 
				ApplicationState.getContext(), KEY_ITEMCATEGORIES_VERSION,
				Integer.class, R.string.itemcategory_error_versionRetrieve);
	}

}
