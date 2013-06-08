/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.activity.async;

import android.app.Activity;

/**
 *  DESCRIPTION: 
 *  This abstract activity provides Progress dialog
 *  functionality. Any activity using Asynchronous task should 
 *  extends this class.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 7, 2013
 */
public abstract class AbstractAsyncActivity extends Activity 
implements IAsyncActivity {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private AsyncActivityHelper helper;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public AbstractAsyncActivity() {
		super();
		helper = new AsyncActivityHelper(this);
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	@Override
	public void showProgressDialog() {
		helper.showProgressDialog();
	}

	@Override
	public void dismissProgressDialog() {
		helper.dismissProgressDialog();
	}

}
