/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.service.proximityalert.triangulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import edu.cmu.cc.slh.model.AccessPoint;
import edu.cmu.cc.slh.model.Section;

import android.net.wifi.WifiManager;

/**
 *  DESCRIPTION: 
 *	
 *  @author Nohsam Park, Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class WCL extends Triangulation {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final float G = 0.6f;
	
	private static final double THRESHOLD_DISTANCE = 300000.0;

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public WCL(WifiManager wm, Map<String, Object> initParams) {
		
		super(wm, initParams);
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public AccessPoint findNearestAccessPoint(List<AccessPoint> accessPoints) {
		
		List<AccessPoint> aps = createAPsCopy(accessPoints);
		
		wifiScanner.scanStart(aps);
		
		aps = wifiScanner.getUpdatedAccessPoints();
		
		if (aps == null || aps.size() < 3) {
			return null;
		}
		
		if (triangulationMethod.equals(TRIANG_METHOD_AWCL)) {
			aps = doAWCL(aps);
		}
		
		AccessPoint nearestAP = null;
		float sumRssi = 0;
		
		// Setting RSSI
		for (AccessPoint ap : aps) {
			
			float newRssi = (float) 
					Math.pow(Math.pow(10, ap.getRssi() / 20), G);
			
			sumRssi += newRssi;
			
			ap.setRssi(newRssi);
		}
		
		
		float x = 0;
		float y = 0;
		
		for (AccessPoint ap : aps) {
			float weight = ap.getRssi() / sumRssi;
			x += ap.getPosX() * weight;
			y += ap.getPosY() * weight;
		}
		
		
		double oldDist = Double.MAX_VALUE;
		double newDist = Double.MAX_VALUE;
		
		for (AccessPoint ap : aps) {
			
			newDist = calculateDestination(ap.getPosX(), ap.getPosY(), x, y);
			
			if (oldDist > newDist) {
				oldDist = newDist;
				
				nearestAP = ap;
				nearestAP.setPosX(x);
				nearestAP.setPosY(y);
				nearestAP.setDistance(newDist);
			}
			
		}
		
		return nearestAP;
	}

	@Override
	public Section findNearestSection(List<Section> sections,
			double x, double y) {
		
		if (sections == null) {
			return null;
		}
		
		Section nearestSection = null;
		
		double oldDist = Double.MAX_VALUE;
		double newDist = Double.MAX_VALUE;
		
		for (Section section : sections) {
			
			newDist = calculateDestination(
					section.getPosX(), section.getPosY(), x, y);
			
			if (newDist >= THRESHOLD_DISTANCE) {
				continue;
			}
			
			if (oldDist > newDist) {
				oldDist = newDist;
				nearestSection = section;
			}
		}
		
		return nearestSection;
	}

	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private List<AccessPoint> doAWCL(List<AccessPoint> accessPoints) {
		
		Comparator<AccessPoint> comparator = new Comparator<AccessPoint>() {
			
			@Override
			public int compare(AccessPoint s1, AccessPoint s2) {
				return (s1.getRssi() >= s2.getRssi()) ? -1 : 1;
			}
		};
		Collections.sort(accessPoints, comparator);
		
		float reduction = 0;
		float q = 0.55f;
		
		for (int i = 0; i < accessPoints.size(); i++) {
			AccessPoint ap = accessPoints.get(i);
			if (i == 0) {
				reduction = ap.getRssi() * q;
			}
			ap.setRssi(ap.getRssi() - reduction);
		}
		
		return accessPoints;
	}
	
	private double calculateDestination(double posX, double posY, 
			double x, double y) {
		
		return Math.sqrt((posX - x)*(posX - x) + (posY - y)*(posY - y));
	}
	
	private List<AccessPoint> createAPsCopy(List<AccessPoint> list) {
		
		List<AccessPoint> newList = new ArrayList<AccessPoint>(list.size());
		for (AccessPoint ap : list) {
			AccessPoint newAP = new AccessPoint();
			newAP.setId(ap.getId());
			newAP.setSsid(ap.getSsid());
			newAP.setPosX(ap.getPosX());
			newAP.setPosY(ap.getPosY());
			newList.add(newAP);
		}
		
		return newList;
	}

}

