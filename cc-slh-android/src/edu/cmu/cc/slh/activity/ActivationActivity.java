/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import edu.cmu.cc.android.activity.async.AbstractAsyncActivity;
import edu.cmu.cc.android.util.WidgetUtils;
import edu.cmu.cc.android.view.ValidatingEditText;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.task.ActivationTask.IActivationTaskCaller;
import android.os.Bundle;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 12, 2013
 */
public class ActivationActivity extends AbstractAsyncActivity 
implements IActivationTaskCaller {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private ValidatingEditText etMembershipID;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PROTECTED METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activation);
		
		etMembershipID = (ValidatingEditText) WidgetUtils
				.getEditText(null, R.id.etMembershipID);
		
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void onAsyncTaskFailed(Class<?> taskClass, Throwable t) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onActivationTaskSucceeded(boolean activated) {
		// TODO Auto-generated method stub
	}
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}