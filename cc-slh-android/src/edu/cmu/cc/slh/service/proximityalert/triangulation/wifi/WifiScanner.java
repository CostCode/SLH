/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.service.proximityalert.triangulation.wifi;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.model.AccessPoint;
import edu.cmu.cc.slh.service.proximityalert.triangulation.Triangulation;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

/**
 *  DESCRIPTION: 
 *	
 *  @author Nohsam Park, Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class WifiScanner {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private WifiManager wifiManager;
	
	private int scanNumber;
	
	private boolean noiseFilterFlag;
	
	private List<AccessPoint> accessPoints;
	
	private List<ScanResult> scanResults;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public WifiScanner(WifiManager wifiManager, Map<String, Object> initParams, 
			List<AccessPoint> accessPoints) {
		
		this.wifiManager = wifiManager;
		this.accessPoints = accessPoints;
		
		this.scanNumber = (Integer) initParams.get(Triangulation.SCAN_NUMBER);
		this.noiseFilterFlag = (Boolean) initParams.get(Triangulation.NOISE_FILTER);
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	public List<AccessPoint> getUpdatedAccessPoints() {
		return accessPoints;
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public void scanStart() {
		Logger.logDebug(getClass(), "WIFI Scan has Started...");
		
		wifiManager.startScan();
		scanResults = wifiManager.getScanResults();
		
		for (int i = 0; i < scanNumber; i++) {
			wifiManager.startScan();
			scanResults.addAll(wifiManager.getScanResults());
		}
		
		parseWIFIScanResult();
		scanResults = null;
	}
	
	public void scanStop() {
		
		Logger.logDebug(getClass(), "WIFI Scan has Stopped...");
		
		wifiManager = null;
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void parseWIFIScanResult() {
		
		sortScanResults();
		
		int rssi = 0;
		int tCount = 0;
		
		ScanResult oldResult = null;
		ScanResult newResult = null;
		
		for (int i = 0; i < scanResults.size(); i++) {
			
			newResult = scanResults.get(i);
			
			if (i > 0 && oldResult.BSSID.compareTo(newResult.BSSID) != 0) {
				if (!noiseFilterFlag && tCount == scanNumber) {
					addAP(oldResult, rssi / tCount);
				}
				rssi = newResult.level;
				tCount = 1;
			} else {
				rssi += newResult.level;
				tCount++;
			}
			
			oldResult = newResult;
			
			if (i == scanResults.size() - 1) {
				if(!noiseFilterFlag || tCount == scanNumber) {  
   					addAP(oldResult, rssi / tCount);
   				}
			}
		}
		
		removeInvalidAPs();
	}
	
	private void sortScanResults() {
		
		Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
			
			@Override
			public int compare(ScanResult s1, ScanResult s2) {
				return (s1.BSSID.compareTo(s2.BSSID));
			}
		};
		
		Collections.sort(scanResults, comparator);
	}
	
	private void addAP(ScanResult scanResult, float rssi) {
		
		for (AccessPoint ap : accessPoints) {
			if (ap.getSsid().equals(scanResult.SSID)) {
				ap.setBssid(scanResult.BSSID);
				ap.setCapabilities(scanResult.capabilities);
				ap.setFrequency(scanResult.frequency);
				ap.setRssi(rssi);
			}
		}
	}
	
	private void removeInvalidAPs() {
		
		for (int i = 0; i < accessPoints.size(); i++) {
			if (accessPoints.get(i).getRssi() == 0) {
				accessPoints.remove(i);
			}
		}
	}

}
