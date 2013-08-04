/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.Warehouse;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class WarehouseAdapter extends AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String KEY_VERSION_WAREHOUSES = "version-warehouses";

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
		
		return saveToSharedPrefs(WarehouseAdapter.class, 
				ApplicationState.getContext(), KEY_VERSION_WAREHOUSES, 
				String.valueOf(version), 
				R.string.warehouse_error_versionPersist);
	}
	
	public static int retrieveVersion() {
		
		String strValue = retrieveFromSharedPrefs(WarehouseAdapter.class, 
				ApplicationState.getContext(), KEY_VERSION_WAREHOUSES, 
				R.string.warehouse_error_versionRetrieve);
		
		if (StringUtils.isNullOrEmpty(strValue)) {
			return -1;
		}
		
		return Integer.parseInt(strValue);
	}
	
	public static Warehouse getDefaultWarehouse() {
		
		long id = SettingsAdapter.retrieveSelectedWarehouseId();
		
		if (id <= 0) {
			return null;
		}
		
		for (Warehouse wh : ApplicationState.getInstance().getWarehouses()) {
			if (wh.getId() == id) {
				return wh;
			}
		}
		
		return null;
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}
