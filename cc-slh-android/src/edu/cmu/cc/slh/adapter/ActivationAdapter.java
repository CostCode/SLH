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
 *  Date: Jun 19, 2013
 */
public class ActivationAdapter extends AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String KEY_ACTIVATION_STATUS = "activation-status";
	
	private static final String KEY_ACTIVATION_MEMBERID = "activation-memberid";

	//-------------------------------------------------------------------------
	// MEMBERSHIPID
	//-------------------------------------------------------------------------	
	
	public static synchronized boolean persistMemberId(
			final String memberId) {
		
		return saveToSharedPrefs(ActivationAdapter.class, 
				ApplicationState.getContext(), KEY_ACTIVATION_MEMBERID, 
				memberId, R.string.activation_error_memberid_persist);
	}
	
	public static String retrieveMemberId() {
		
		return (String) retrieveFromSharedPrefs(ActivationAdapter.class,
				ApplicationState.getContext(), KEY_ACTIVATION_MEMBERID, 
				String.class,
				R.string.activation_error_memberid_retrieve);
	}
	
	//-------------------------------------------------------------------------
	// ACTIVATION STATUS
	//-------------------------------------------------------------------------	
	
	public static synchronized boolean persistActivationStatus(boolean status) {
		
		return saveToSharedPrefs(ActivationAdapter.class,
				ApplicationState.getContext(), KEY_ACTIVATION_STATUS, 
				Boolean.valueOf(status), R.string.activation_error_status_persist);
	}
	
	public static boolean retrieveActivationStatus() {
		
		return (Boolean) retrieveFromSharedPrefs(ActivationAdapter.class,
				ApplicationState.getContext(), KEY_ACTIVATION_STATUS, 
				Boolean.class, R.string.activation_error_status_retrieve);
	}

}
