/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.util;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 14, 2013
 */
public class StringUtils {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

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
	
	/**
	 * Checks whether the given string is empty or null
	 * @param value - a string to be validated
	 * @return <b>true</b> - if the given string is null or empty, 
	 * <b>false</b> - if not
	 */
	public static boolean isNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}
	
	/**
	 * Limits the length of the string to the given max length and adds 
	 * additional ending 
	 * @param originalValue - a string to be limited
	 * @param maxLength - maximum length of the final string
	 * @param appendChars - a characters to be added to the end
	 * @return limited string
	 */
	public static String limitLength(String originalValue, int maxLength, 
			String appendChars) {
		
		if (originalValue == null) {
			return "";
		}
		if (originalValue.length() <= maxLength) {
			return originalValue;
		}
		if (appendChars == null) {
			appendChars = "";
		}
		
		String limitedValue = originalValue
				.substring(0, maxLength - appendChars.length() - 1) 
				+ appendChars;
		
		return limitedValue;
	}

	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
