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

	
	/**
	 * Web service description.
	 */
	public static abstract class WebService {
		
		/** Web Service url */
		public static final String URL = "http://shltestweb.appspot.com/shlappeng";
		
		/** Web Service namespace */
		public static final String NAMESPACE = "http://costcode.mse.cmu.edu/";
		
	}
	
	
	/**
	 * Available Service Methods.
	 */
	public static abstract class Methods {
		
		/** User registration method */
		public static final String VALIDATE_MEMBERSHIP = "validate-membership";
		
	}
	
	
	/**
	 * User data descriptions.
	 */
	public static abstract class User {
		
		/** Costco Membership id */
		public static final String MEMBER_ID = "memberid";
		
		/** Username */
		public static final String USERNAME = "username";
		
		/** First name */
		public static final String FIRSTNAME = "firstname";
		
		/** Last name */
		public static final String LASTNAME = "lastname";
		
		/** Password */
		public static final String PASSWORD = "password";
		
		
		
		/** Membership validity */
		public static final String MEMBERSHIP_VALIDITY = "membership-validity";
		
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