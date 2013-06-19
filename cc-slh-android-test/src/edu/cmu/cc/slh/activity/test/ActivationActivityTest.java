/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity.test;

import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.activity.ActivationActivity;
import edu.cmu.cc.slh.adapter.ActivationAdapter;
import edu.cmu.cc.slh.view.adapter.ActivationViewAdapter;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;

/**
 *  DESCRIPTION: Activation activity test class
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 18, 2013
 */
public class ActivationActivityTest 
extends ActivityInstrumentationTestCase2<ActivationActivity> {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private ActivationActivity activationActivity;
	
	private ApplicationState applicationState;
	
	private String[] viewValues;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ActivationActivityTest() {
		super(ActivationActivity.class);
	}
	
	//-------------------------------------------------------------------------
	// TEST METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * This will be called before each test method.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		activationActivity = getActivity();
		
		applicationState = 
				(ApplicationState) activationActivity.getApplication();
		
		//-----------------------------------------------
		// Assigning valid Costco membership id value:
		// 12 digits, starting from 1,3, or 8.
		//-----------------------------------------------
		viewValues = new String[] {"123456789012"};
	}
	
	/**
	 * Testing Initial conditions
	 */
	public void testPreConditions() {
		//-----------------------------------------------
		// Initially, user app is not activated
		//-----------------------------------------------
		assertFalse(ActivationAdapter
				.retrieveActivationStatus(applicationState));
	}
	
	@UiThreadTest
	public void testValidValues() {
		
		assignViewValues(viewValues);
		
		//-----------------------------------------------
		// Making sure that membership id was correctly 
		// validated: expecting true
		//-----------------------------------------------
		assertTrue(isViewValid());
	}
	
	@UiThreadTest
	public void testInvalidValues() {
		
		//-----------------------------------------------
		// Assigning incorrect membership id 
		//-----------------------------------------------
		assignViewValues(new String[] {"6542"});
		
		//-----------------------------------------------
		// Making sure that membership id was correctly 
		// validated: expecting false
		//-----------------------------------------------
		assertFalse(isViewValid());
	}
	
	@UiThreadTest
	public void testStateDestroyedAndCreated() {
		
		assignViewValues(viewValues);
		
		String membershipIDBefore = ActivationViewAdapter
				.getMembershipID(activationActivity.getActivationView());
		 
		activationActivity.finish();
		activationActivity = getActivity();
		
		String membershipIDAfter = ActivationViewAdapter
				.getMembershipID(activationActivity.getActivationView());
		
		assertEquals(membershipIDBefore, membershipIDAfter);
	}
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------
	
	private void assignViewValues(String[] values) {
		setEditText(R.id.etActivationMembershipID, values[0]);
	}
	
	private void setEditText(int id, String value) {
		EditText et = (EditText) activationActivity.findViewById(id);
		et.setText(value);
	}
	
	private boolean isViewValid() {
		ActivationViewAdapter viewAdapter = 
				activationActivity.getActivationViewAdapter();
		
		viewAdapter.validateAllViews();
		
		return viewAdapter.areAllViewsValid();
	}

}