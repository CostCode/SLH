/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 22, 2013
 */
public class ProximityAlertAdapter extends AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static String KEY_FLOORPLAN_VERSION = "version-floorplan";

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
	
	public static synchronized boolean persistFloorplanVersion(int version) {
		
		return saveToSharedPrefs(ProximityAlertAdapter.class, 
				ApplicationState.getContext(), KEY_FLOORPLAN_VERSION, 
				String.valueOf(version), R.string.sl_all_error_versionPersist);
	}
	
	public static int retrieveFloorplanVersion() {
		
		String strValue = retrieveFromSharedPrefs(ProximityAlertAdapter.class,
				ApplicationState.getContext(), KEY_FLOORPLAN_VERSION, 
				R.string.sl_all_error_versionRetrieve);
		
		if (StringUtils.isNullOrEmpty(strValue)) {
			return 0;
		}
		
		return Integer.parseInt(strValue);
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}
