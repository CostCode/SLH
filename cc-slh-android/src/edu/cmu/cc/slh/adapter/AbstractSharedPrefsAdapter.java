/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import android.content.Context;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.SharedPrefsAdapter;
import edu.cmu.cc.android.util.StringUtils;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
abstract class AbstractSharedPrefsAdapter {

	//-------------------------------------------------------------------------
	// PROTECTED METHODS
	//-------------------------------------------------------------------------
	
	protected static boolean saveToSharedPrefs(Class<?> caller, Context ctx, 
			String key, Object value, int errMsgResId) {
		
		try {
			
			if (value.getClass() == String.class) {
				return SharedPrefsAdapter.persistString(ctx, key, (String)value);
			}
			if (value.getClass() == Long.class) {
				return SharedPrefsAdapter.persistLong(ctx, key, (Long)value);
			}
			if (value.getClass() == Integer.class) {
				return SharedPrefsAdapter.persistInt(ctx, key, (Integer)value);
			}
			if (value.getClass() == Boolean.class) {
				return SharedPrefsAdapter.persistBoolean(ctx, key, (Boolean)value);
			}
			
			throw new IllegalArgumentException("Unsupported value type: " + value);
			
		} catch (Throwable t) {
			String errMsg = getErrorMessage(ctx, errMsgResId, t);
			Logger.logErrorAndAlert(ctx, caller, errMsg, t);
		}
		
		return false;
	}
	
	protected static Object retrieveFromSharedPrefs(Class<?> caller, Context ctx, 
			String key, Class<?> valueType, int errMsgResId) {
		
		try {
			
			if (valueType == String.class) {
				return SharedPrefsAdapter.retrieveString(ctx, key);
			}
			if (valueType == Long.class) {
				return SharedPrefsAdapter.retrieveLong(ctx, key);
			}
			if (valueType == Integer.class) {
				return SharedPrefsAdapter.retrieveInt(ctx, key);
			}
			if (valueType == Boolean.class) {
				return SharedPrefsAdapter.retrieveBoolean(ctx, key);
			}
			
			throw new IllegalArgumentException("Unsupported value type: " + 
					valueType);
			
		} catch (Throwable t) {
			String errMsg = getErrorMessage(ctx, errMsgResId, t);
			Logger.logErrorAndAlert(ctx, caller, errMsg, t);
		}
		
		return null;
	}
	
	protected static String getErrorMessage(Context ctx, 
			int errMsgResID, Throwable t) {
		
		return StringUtils.getLimitedString(
				ctx.getString(errMsgResID, t.getMessage()), 200, "...");
	}

}
