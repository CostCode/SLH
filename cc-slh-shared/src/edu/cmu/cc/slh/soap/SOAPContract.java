/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.soap;

/**
 *  DESCRIPTION: This class provides common data names for both 
 *  android client and server applications.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 5, 2013
 */
public class SOAPContract {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	
	/** Web Service url */
	public static final String URL = "http://shltestweb.appspot.com/shlappeng";
	
	/** Web Service namespace */
	public static final String NAMESPACE = "http://costcode.mse.cmu.edu/";
	
	/**
	 * User data descriptions.
	 */
	public static abstract class Membership {
		
		public static final String METHOD_VALIDATE = "validateMembership";
		
		public static final String VALIDATION_RESULT = "validateMembershipResult";
		
		public static final String MEMBERSHIP_ID = "membership_id";
		
	}
	
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}