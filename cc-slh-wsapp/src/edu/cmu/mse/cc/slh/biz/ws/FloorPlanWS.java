/**
 * %W% %E% M. A. Riveros T.
 *
 * Copyright (c) 2012-2013 MSE, Carnegie Mellon University. All Rights Reserved.
 * 
 */

package edu.cmu.mse.cc.slh.biz.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import edu.cmu.mse.cc.slh.integration.dao.AccessPointDAO;
import edu.cmu.mse.cc.slh.integration.dao.FloorPlanDAO;

/**
 * WS offering floor plans related services
 * 
 * @version 1.0.0. 21 Jun 2013
 * @author M. A. Riveros T.
 */
@WebService(portName = "FloorPlanWSPort", 
	serviceName = "FloorPlanWS", 
	targetNamespace = "http://ws.biz.slh.cc.mse.cmu.edu/", 
	endpointInterface = "edu.cmu.mse.cc.slh.biz.ws.FloorPlanWS")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class FloorPlanWS {
	
	private final static String ACCESSPOINTS_CATEGORYNAME = "Access Points";

	@WebMethod(action = "checkVersion")
	@WebResult(name = "version")
	public String checkVersion(
			@WebParam(name = "warehouseID") String warehouseID) {

		if (warehouseID == null || warehouseID.isEmpty())
			return null;

		FloorPlanDAO floorPlanDAO = FloorPlanDAO.getInstance();
		String version = floorPlanDAO.getVersion(warehouseID);

		return version;
	}

	/**
	 * 
	 * Indicates the location of APs inside a particular warehouse
	 * 
	 * @param warehouseID
	 *            Warehouse identifier
	 * 
	 * @return accessPoints A string of access points within the warehouse
	 * 
	 */
	@WebMethod(action = "APsLocation")
	@WebResult(name = "APsLocation")
	public String aPsLocation(@WebParam(name = "warehouseID") String warehouseID) {

		if (warehouseID == null || warehouseID.isEmpty())
			return null;

		String aPsLocation = "<ssid>ssid_a</ssid><posx>0</posx><posy>0</posy>"
				+ "<ssid>ssid_b</ssid><posx>0</posx><posy>10</posy>"
				+ "<ssid>ssid_c</ssid><posx>10</posx><posy>0</posy>"
				+ "<ssid>ssid_d</ssid><posx>10</posx><posy>10</posy>";

		AccessPointDAO accessPointDAO = AccessPointDAO.getInstance();
		aPsLocation = accessPointDAO.getHotSpotsByCategoryName(warehouseID, ACCESSPOINTS_CATEGORYNAME);

		return aPsLocation;
	}

	/**
	 * Obtains sections of a particular warehouse
	 * 
	 * @param warehouseID
	 *            Warehouse identifier.
	 * 
	 * @return sectionsLocation A string of sections within the warehouse
	 * 
	 */
	@WebMethod(action = "SectionsLocation")
	@WebResult(name = "SectionsLocation")
	public String sectionsLocation(
			@WebParam(name = "warehouseID") String warehouseID) {

		if (warehouseID == null || warehouseID.isEmpty())
			return null;

		String sectionsLocation = "<ssid>ssid_a</ssid><posx>0</posx><posy>0</posy>"
				+ "<ssid>ssid_b</ssid><posx>0</posx><posy>10</posy>"
				+ "<ssid>ssid_c</ssid><posx>10</posx><posy>0</posy>"
				+ "<ssid>ssid_d</ssid><posx>10</posx><posy>10</posy>";

		AccessPointDAO accessPointDAO = AccessPointDAO.getInstance();
		sectionsLocation = accessPointDAO.getSectionsByWarehouseID(warehouseID);

		return sectionsLocation;
	}

}
