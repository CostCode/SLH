/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity.test;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.activity.ActivationActivity;
import android.test.ActivityInstrumentationTestCase2;

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
	}
	
	public void testWebService() {
		assertTrue(checkWS());
	}
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------
	
	private boolean checkWS() {
		
		SoapWebService service = new SoapWebService(
				"http://ws.biz.slh.cc.mse.cmu.edu/", 
				"http://slhwsapp-costcode.rhcloud.com:80/MemberWS");
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("membershipID", "123456789012");
		
		try {
			
			SoapObject response = service.invokeMethod(
					"retrieveMemberVersion", args);
			
			Object complexProperty = response.getProperty("RetrieveMemberVersionResponse");
			
			if (complexProperty instanceof SoapObject) {
				SoapObject complexXML = (SoapObject) complexProperty;
				
				String version = complexXML.getPropertyAsString("memberVersion");
				String exception = complexXML.getPropertyAsString("exception");
				
				Logger.logDebug(getClass(), String.format("%s : %s", version, exception));
			}
			
		} catch (Throwable t) {
			Logger.logError(getClass(), t);
			return false;
		}
		
		return true;
	}

}