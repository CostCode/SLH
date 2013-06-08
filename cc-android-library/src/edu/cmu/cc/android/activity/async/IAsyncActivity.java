/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.activity.async;

/**
 *  DESCRIPTION: All activities that are going to use asynchronous
 *  tasks should implement this interface. It specifies progress dialog
 *  window methods.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 7, 2013
 */
public interface IAsyncActivity {
	
	/**
	 * Show progress dialog
	 */
	public void showProgressDialog();
	
	/**
	 * Dismiss progress dialog
	 */
	public void dismissProgressDialog();

}
