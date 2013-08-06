/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


/**
 *  DESCRIPTION: This class provides easy mechanism to store and retrieve
 *  data from the shared preferences file.
 *	
 *  @author Azamat Samiyev
 *	@version 2.0
 *  Date: Jun 14, 2013
 */
public class SharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static boolean persistString(Context ctx, String key, String value) {
		Editor editor = getPreferences(ctx).edit();
		editor.putString(key, value);
		return editor.commit();
	}
	public static String retrieveString(Context ctx, String key) {
		return getPreferences(ctx).getString(key, null);
	}
	
	public static boolean persistLong(Context ctx, String key, long value) {
		Editor editor = getPreferences(ctx).edit();
		editor.putLong(key, value);
		return editor.commit();
	}
	public static long retrieveLong(Context ctx, String key) {
		return getPreferences(ctx).getLong(key, -1);
	}
	
	public static boolean persistInt(Context ctx, String key, int value) {
		Editor editor = getPreferences(ctx).edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	public static int retrieveInt(Context ctx, String key) {
		return getPreferences(ctx).getInt(key, -1);
	}
	
	public static boolean persistBoolean(Context ctx, String key, boolean value) {
		Editor editor = getPreferences(ctx).edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}
	public static boolean retrieveBoolean(Context ctx, String key) {
		return getPreferences(ctx).getBoolean(key, false);
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private static SharedPreferences getPreferences(Context ctx) {
		return PreferenceManager.getDefaultSharedPreferences(ctx);
	}
}
