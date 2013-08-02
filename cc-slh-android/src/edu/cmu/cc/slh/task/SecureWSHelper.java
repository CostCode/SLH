/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import edu.cmu.cc.slh.R;

/**
 *  This class enables web service communicating classes to include
 *  user membership id into the request arguments.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public abstract class SecureWSHelper {

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	/**
	 * All the web service communicating tasks call this method
	 * to initialize web service request parameters with costco
	 * membership id.
	 * 
	 * @param ctx - android context
	 * @param memberId - membership id
	 * @return initialized request arguments
	 */
	public static synchronized Map<String, String> initWSArguments(
			Context ctx, String memberId) {
		
		Map<String, String> args = new HashMap<String, String>(1);
		args.put(ctx.getString(
				R.string.ws_property_memberId), memberId);
		
		return args;
	}

}