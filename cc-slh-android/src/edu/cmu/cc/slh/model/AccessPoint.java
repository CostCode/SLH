/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;

/**
 *  DESCRIPTION: AccessPoint model class
 *	
 *  @author Nohsam Park, Azamat Samiyev
 *	@version 2.0
 *  Date: Jul 15, 2013
 */
public class AccessPoint extends BaseEntity {
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private String bssid;
	
	private String ssid;
	
	private String capabilities;
	
	private int frequency;
	
	private float rssi;
	
	private float posX;
	
	private float posY;
	
	private double distance;
	
	private String description;

	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public AccessPoint() {}
	
	public AccessPoint(String bssid, String ssid, String capabilities, 
			int frequency, float rssi, float posX, float posY) {
		this.bssid = bssid;
		this.ssid = ssid;
		this.capabilities = capabilities;
		this.frequency = frequency;
		this.rssi = rssi;
		this.posX = posX;
		this.posY = posY;
		this.distance = 0.0F; 
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	public String getBssid() {
		return bssid;
	}
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getCapabilities() {
		return capabilities;
	}
	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}

	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public float getRssi() {
		return rssi;
	}
	public void setRssi(float rssi) {
		this.rssi = rssi;
	}

	public float getPosX() {
		return posX;
	}
	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}
	public void setPosY(float posY) {
		this.posY = posY;
	}

	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(", ");
		builder.append(bssid);
		builder.append(", ");
		builder.append(ssid);
		builder.append(", ");
		builder.append(capabilities);
		builder.append(", ");
		builder.append(posX);
		builder.append(", ");
		builder.append(posY);
		
		return builder.toString();
	}
	
}
