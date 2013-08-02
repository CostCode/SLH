/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.activity.async;

/**
 *  All activities that are going to use asynchronous
 *  tasks should implement this interface. It specifies progress dialog
 *  window methods.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 7, 2013
 */
public interface IAsyncActivity {
	
	/**
	 * Show the progress dialog.
	 * 
	 * @param titleResID - dialog title
	 * @param textResID - dialog text
	 */
	public void showProgressDialog(int titleResID, int textResID);
	
	/**
	 * Show the progress dialog.
	 * 
	 * @param textResID - dialog text
	 */
	public void showProgressDialog(int textResID);
	
	/**
	 * Dismiss progress dialog
	 */
	public void dismissProgressDialog();
	
	/**
	 * Method which is called when an asynchronous task successfully 
	 * finished its execution. 
	 * 
	 * @param taskClass - class of the task
	 */
	public void onAsyncTaskSucceeded(Class<?> taskClass);
	
	/**
	 * This method will be called by the Async Task classes to notify the
	 * async activity about the error happened during the task execution.
	 * 
	 * @param taskClass - calling task class
	 * @param t - exception object
	 */
	public void onAsyncTaskFailed(Class<?> taskClass, Throwable t);

}
