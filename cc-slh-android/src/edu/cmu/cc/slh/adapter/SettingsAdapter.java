/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import android.content.Context;
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
public class SettingsAdapter extends AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// SETTING: WAREHOUSE
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistSelectedWarehouseId(long id) {
		
		Context ctx = ApplicationState.getContext();
		
		return saveToSharedPrefs(SettingsAdapter.class, 
				ctx, ctx.getString(R.string.setting_warehouse_key),
				String.valueOf(id), R.string.settings_warehouse_error_persist);
	}
	
	public static long retrieveSelectedWarehouseId() {
		
		Context ctx = ApplicationState.getContext();
		
		String strId = retrieveFromSharedPrefs(SettingsAdapter.class, 
				ctx, ctx.getString(R.string.setting_warehouse_key),
				R.string.settings_warehouse_error_retrieve);
		
		if (StringUtils.isNullOrEmpty(strId)) {
			return -1;
		}
		
		return Long.parseLong(strId);
	}
	
	//-------------------------------------------------------------------------
	// SETTING: PROXIMITY ALERT ENABLED
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistProximityAlertEnabled(
			boolean enabled) {
		
		Context ctx = ApplicationState.getContext();
		
		return saveToSharedPrefs(SettingsAdapter.class, 
				ctx, ctx.getString(R.string.setting_proximity_enable_key),
				String.valueOf(enabled), 
				R.string.settings_proximityalert_enabled_error_persist);
	}
	
	public static boolean retrieveProximityAlertEnabled() {
		
		Context ctx = ApplicationState.getContext();
		
		String strEnabled = retrieveFromSharedPrefs(SettingsAdapter.class,
				ctx, ctx.getString(R.string.setting_proximity_enable_key),
				R.string.settings_proximityalert_enabled_error_retrieve);
		
		if (!StringUtils.isNullOrEmpty(strEnabled)) {
			return Boolean.parseBoolean(strEnabled);
		}
		
		return false;
	}
	
	//-------------------------------------------------------------------------
	// SETTING: PROXIMITY ALERT METHOD [GPS, WIFI]
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistProximityAlertMethod(
			String method) {
		
		Context ctx = ApplicationState.getContext();
		
		return saveToSharedPrefs(SettingsAdapter.class, 
				ctx, ctx.getString(R.string.setting_proximity_method_key), 
				method, R.string.settings_proximityalert_method_error_persist);
	}
	
	public static String retrieveProximityAlertMethod() {
		
		Context ctx = ApplicationState.getContext();
		
		return retrieveFromSharedPrefs(SettingsAdapter.class,
				ctx, ctx.getString(R.string.setting_proximity_method_key), 
				R.string.settings_proximityalert_method_error_retrieve);
	}
	
	//-------------------------------------------------------------------------
	// SETTING: PROXIMITY ALERT VIBRATION
	//-------------------------------------------------------------------------
	
	public static synchronized boolean persistProximityAlertVibration(
			boolean status) {
		
		Context ctx = ApplicationState.getContext();
		
		return saveToSharedPrefs(SettingsAdapter.class, 
				ctx, ctx.getString(R.string.setting_proximity_alert_vibration_key), 
				String.valueOf(status), 
				R.string.settings_proximityalert_vibration_error_persist);
	}
	
	public static boolean retrieveProximityAlertVibration() {
		
		Context ctx = ApplicationState.getContext();
		
		String strStatus = retrieveFromSharedPrefs(SettingsAdapter.class,
				ctx, ctx.getString(R.string.setting_proximity_alert_vibration_key), 
				R.string.settings_proximityalert_vibration_error_retrieve);
		
		if (!StringUtils.isNullOrEmpty(strStatus)) {
			return Boolean.parseBoolean(strStatus);
		}
		
		return false;
	}

}
