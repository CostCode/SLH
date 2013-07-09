/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.adapter;

import android.content.Context;
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
	
	protected static String getErrorMessage(Context ctx, 
			int errMsgResID, Throwable t) {
		
		return StringUtils.getLimitedString(
				ctx.getString(errMsgResID, t.getMessage()), 200, "...");
	}

}
