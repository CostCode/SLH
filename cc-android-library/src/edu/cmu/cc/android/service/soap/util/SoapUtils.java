/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.service.soap.util;

import org.ksoap2.serialization.SoapObject;

import edu.cmu.cc.android.R;
import edu.cmu.cc.android.util.StringUtils;

import android.content.Context;

/**
 *  Soap utility class which allows to read soap properties as 
 *  primitive java values.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public abstract class SoapUtils {
	
	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String EXCEPTION_BIZ_PREFIX = "Biz:";

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static synchronized long getLongPropertyValue(SoapObject source, 
			String propertyName) {
		
		checkPropertyPresence(source, propertyName);
		
		return Long.parseLong(source.getPropertyAsString(propertyName));
	}
	
	public static synchronized int getIntPropertyValue(SoapObject source, 
			String propertyName) {
		
		checkPropertyPresence(source, propertyName);
		
		return Integer.parseInt(source.getPropertyAsString(propertyName));
	}
	
	public static synchronized double getDoublePropertyValue(
			SoapObject source, String propertyName) {
		
		checkPropertyPresence(source, propertyName);
		
		return Double.parseDouble(source.getPropertyAsString(propertyName));
	}
	
	public static synchronized boolean getBooleanPropertyValue(
			SoapObject source, String propertyName) {
		
		checkPropertyPresence(source, propertyName);
		
		return Boolean.parseBoolean(source.getPropertyAsString(propertyName));
	}
	
	public static synchronized SoapObject getComplexPropertyValue(
			SoapObject source, String propertyName) {
		
		checkPropertyPresence(source, propertyName);
		
		Object property = source.getProperty(propertyName);
		
		if (!isComplexProperty(property)) {
			throw new IllegalStateException(String.format("Complext " +
					"property was expected by name [%s]", propertyName));
		}
		
		return (SoapObject) property;
	}
	
	public static synchronized boolean isComplexProperty(Object property) {
		return (property instanceof SoapObject);
	}
	
	public static synchronized String getException(
			Context ctx, SoapObject source) {
		
		checkPropertyPresence(source, 
				ctx.getString(R.string.ws_property_exception));
		
		return source.getPropertyAsString(
				ctx.getString(R.string.ws_property_exception));
	}
	
	public static synchronized boolean hasException(
			Context ctx, SoapObject source) {
		return source.hasProperty(ctx.getString(R.string.ws_property_exception));
	}
	
	public static boolean isBisunessException(String exception) {
		return exception.startsWith(EXCEPTION_BIZ_PREFIX);
	}
	
	public static String getBusinessException(String exception) {
		
		if (isBisunessException(exception)) {
			return exception.replaceAll(EXCEPTION_BIZ_PREFIX, "").trim();
		}
		
		return null;
	}
	
	public static void checkForException(Context ctx, SoapObject source) {
		
		if (hasException(ctx, source)) {
			
			String exception = getBusinessException(getException(ctx, source));
			
			if (!StringUtils.isNullOrEmpty(exception)) {
				throw new IllegalStateException(exception);
			} else {
				throw new IllegalStateException(
						SoapUtils.getException(ctx, source));
			}
		}
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private static void checkPropertyPresence(SoapObject source, 
			String propertyName) {
		
		if (!source.hasProperty(propertyName)) {
			throw new IllegalStateException(String.format("Property [%s] " +
					"is missing from the SoapObject", propertyName));
		}
	}

}
