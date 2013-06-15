/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.activity.async;

import android.app.ListActivity;

/**
 *  DESCRIPTION: 
 *	This abstract list activity provides Progress dialog
 *  functionality. Any list activity using Asynchronous task should 
 *  extends this class.
 *
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 7, 2013
 */
public abstract class AbstractAsyncListActivity extends ListActivity 
implements IAsyncActivity {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private AsyncActivityHelper helper;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public AbstractAsyncListActivity() {
		super();
		helper = new AsyncActivityHelper(this);
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	@Override
	public void showProgressDialog(int titleResID, int textResID) {
		helper.showProgressDialog(titleResID, textResID);
	}

	@Override
	public void dismissProgressDialog() {
		helper.dismissProgressDialog();
	}
	
}
