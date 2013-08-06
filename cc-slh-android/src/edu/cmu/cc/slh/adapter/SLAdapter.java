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
 *  Date: Jun 21, 2013
 */
public class SLAdapter extends AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String KEY_MEMBER_VERSION = "version-member";

	//-------------------------------------------------------------------------
	// MEMBER VERSION
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistMemberVersion(int version) {
		
		return saveToSharedPrefs(SLAdapter.class, ApplicationState.getContext(), 
				KEY_MEMBER_VERSION, Integer.valueOf(version), 
				R.string.sl_all_error_versionPersist);
	}
	
	public static int retrieveMemberVersion() {
		
		return (Integer) retrieveFromSharedPrefs(SLAdapter.class, 
				ApplicationState.getContext(), KEY_MEMBER_VERSION, 
				Integer.class, R.string.sl_all_error_versionRetrieve);
	}

}