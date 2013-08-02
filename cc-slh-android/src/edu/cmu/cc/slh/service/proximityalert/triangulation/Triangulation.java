/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.service.proximityalert.triangulation;

import java.util.List;
import java.util.Map;

import android.net.wifi.WifiManager;

import edu.cmu.cc.slh.model.AccessPoint;
import edu.cmu.cc.slh.model.Section;
import edu.cmu.cc.slh.service.proximityalert.triangulation.wifi.WifiScanner;


/**
 *  DESCRIPTION: 
 *	
 *  @author Nohsam Park
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public abstract class Triangulation {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	public static final String TRIANG_METHOD = "TRIANG_METHOD";
	
	public static final String TRIANG_METHOD_WCL = "WCL";
	
	public static final String TRIANG_METHOD_AWCL = "AWCL";
	
	
	public static final String SCAN_NUMBER = "SCAN_NUMBER";
	
	public static final int DEFAULT_SCAN_NUMBER = 5;
	
	
	public static final String NOISE_FILTER = "NOISE_FILTER";
	
	public static final boolean DEFAULT_NOISE_FILTER = true;

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	protected WifiScanner wifiScanner;
	
	protected String triangulationMethod;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public Triangulation(WifiManager wm, Map<String, Object> initParams, 
			List<AccessPoint> accessPoints) {
		
		triangulationMethod = (String) initParams.get(TRIANG_METHOD); 
		wifiScanner = new WifiScanner(wm, initParams, accessPoints);
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public AccessPoint findNearestAccessPoint() {
		wifiScanner.scanStart();
		
		return wifiScanner.getUpdatedAccessPoints().get(0);
	}
	
	public abstract Section findNearestSection(
			List<Section> sections, double x, double y);
	
	public void stop() {
		wifiScanner.scanStop();
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}