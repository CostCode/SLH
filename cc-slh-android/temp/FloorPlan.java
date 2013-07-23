/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.service.proximityalert;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.util.Log;

import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.dao.AccessPointDAO;
import edu.cmu.cc.slh.dao.ItemCategoryDAO;
import edu.cmu.cc.slh.model.AccessPoint;
import edu.cmu.cc.slh.model.ItemCategory;

/**
 * 
 *  DESCRIPTION: Call WebService for access points
 *	
 *  @author Nohsam Park, Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 15, 2013
 */
public class FloorPlan extends Thread {
	
	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	private final static String INFO_NAME = "floorplan";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private ItemCategoryDAO itemCategoryDAO;
	
	private AccessPointDAO accessPointDAO;
	
	private List<ItemCategory> categoryList;
	
	private List<AccessPoint> apList;
	
	private SoapWebService webService;
	
	private Map<String, String> wsArguments;
	
	

	/** Constructor for floorplan
	 * 
	 */
	public FloorPlan(Context ctx) {
		
		webService = new SoapWebService(
				ctx.getString(R.string.ws_floorplan_namespace), 
				ctx.getString(R.string.ws_floorplan_url));
		
		itemCategoryDAO = new ItemCategoryDAO();
		accessPointDAO = new AccessPointDAO();
		
		this.ctx = ctx;
	}
	
	/** Get Access Points information
	 * @return List<AccessPoint>
	 */
	public List<AccessPoint> getAccessPoints() {
		if(apList == null) {
			apList = accessPointDAO.getAll();
		}
		return apList;
	}
	
	/** Save Access Points information to the DB
	 */
	private void saveAccessPoints() {
		
		accessPointDAO.saveAll(apList);
	}
	
	/** Get Category information
	 * @return List<AccessPoint>
	 */
	public List<ItemCategory> getCategories() {
		
		if(categoryList == null) {
			categoryList = itemCategoryDAO.getAll();
		}
		
		return categoryList;
	}
	
	/** Save Categories information to the DB
	 */
	private void saveCategories() {
		
		// Insert apList into db
		for(Iterator<Category> itr = categoryList.iterator(); itr.hasNext(); ) {
			Category category = itr.next();
			db.dbCreateCategory(category);
		}
	}
	
	/** Parse results and retrieve Access Point data
	 * 
	 * @param soap The returned SOAP object
	 */
	private void getAPsFromSoap(SoapObject soap) {
		if(soap == null) {
			apList = null;
			return;
		}
		
//		 Delete the existing AP information
		db.open();
		db.dbDeleteAccessPoint();
		db.close();
		
		// TODO: uncomment the next line
//		if(soap == null) {
//			String testString = "<ssid>food</ssid><posx>0.0</posx><posy>0.0</posy><ssid>CMU-SECURE</ssid><posx>20.0</posx><posy>0.5</posy>" +
//					"<ssid>PSC</ssid><posx>20.5</posx><posy>-20</posy><ssid>CMU</ssid><posx>0</posx><posy>-20.4</posy>";
//			parseXML(testString);
//		}
		parseXML(soap.getPrimitivePropertyAsString("APsLocation"));
		
		// after parse, insert into DB
		if(apList != null) {
			saveAccessPoints();
		}
	}
	
	/** Parse results and retrieve category data
	 * 
	 * @param soap The returned SOAP object
	 */
	private void getCategoriesFromSoap(SoapObject soap) {
		if(soap == null) {
			categoryList =null;
			return;
		}
		
//		 Delete the existing category information
		db.open();
		db.dbDeleteCategory();
		db.close();
		
		// TODO: uncomment the next line
//		if(soap == null) {
//			String testString = "<sid>food</sid><posx>5.3</posx><posy>-3.5</posy>" +
//					"<sid>electronics</sid><posx>13</posx><posy>-7.3</posy>" +
//					"<sid>furniture</sid><posx>13</posx><posy>-7.3</posy>" +
//					"<sid>clothes</sid><posx>18</posx><posy>-15</posy>";
//			parseXML(testString);
//		}
		parseXML(soap.getPrimitivePropertyAsString("SectionsLocation"));
		
		// after parse, insert into DB
		if(categoryList != null) {
			saveCategories();
		}
	}

	
	private void parseXML(String result) {
		// Result format
		// <ssid>name</ssid><posx>x</posx><posy>y</posy><category>name</ssid><posx>x</posx><posy>y</posy>
		
		String[] splits = result.replaceAll("<", "").replaceAll("/", ">").split(">");
		int index = 0;
		
		apList = new ArrayList<AccessPoint>();
		
		categoryList = new ArrayList<Category>();
		
		AccessPoint ap = null;
		Category category = null;
		boolean ssidFlag = true;
		
		String categoryName = null;
		double posx=0, posy=0;
		while(true) {

        	if(splits[index].toLowerCase().equals("ssid")) {
        		ssidFlag = true; // Data is AP
        		
        		ap = new AccessPoint(); // create new AP
        		ap.setSsid(splits[++index]); // set the name of AP
        		index += 2; // skip next xml tag: <ssid>CMU</ssid>
        	}
        	else if(splits[index].toLowerCase().equals("sid")) {
//        	else if(splits[index].toLowerCase().equals("category")) {
        		ssidFlag = false; // Data is category
        		
        		categoryName = splits[++index];
        		index += 2; // skip next xml tag: <category>food</category>
        	}
        	else if(splits[index].toLowerCase().equals("posx")) {
        		if(ssidFlag) { // if data is AP
	        		ap.setPosX(Float.parseFloat(splits[++index]));
        		}
        		else { // if data is category
        			posx = Double.parseDouble(splits[++index]);
        		}
	        		index += 2; // skip next xml tag

        	}
        	else if(splits[index].toLowerCase().equals("posy")) {
        		if(ssidFlag) { // if data is AP
	        		ap.setPosY(Float.parseFloat(splits[++index]));
	        		apList.add(ap);
        		}
        		else { // if data is category
        			posy = Double.parseDouble(splits[++index]);
        			categoryList.add(new Category(categoryName, new Location(posx, posy)));
        		}

        		index += 2; // skip next xml tag: <ssid>CMU</ssid>
        	}
        	else {
        		index++;
        	}
        	if(index >= splits.length)
        		break;
        }
		
		
		
	}

	/** Set Web Service Inputs
	 * 
	 */
	private void setWSInputs() {
		wsArguments = new HashMap<String, String>(1);
		wsArguments.put(ctx.getString(R.string.ws_floorplan_warehouseid), "2");
	}
	
	private boolean checkFloorplanVersion(SoapObject soapObject) {
		if(soapObject == null) {
			return true; // use the existing version of floor plan regardless of error
			// make dummy version information
//			soapObject = new SoapObject();
//			soapObject.addProperty("version", "1.0");
		}
		
		db.open();
		String oldVersion = db.dbGetVersion(INFO_NAME);
		
		// Check version
		boolean result = false;
		if(oldVersion != null) {
			result = oldVersion.equals(soapObject.getPropertyAsString("version"));
		}
		// If not equal
		if(!result) {
			// insert or update
			db.dbSetVersion(INFO_NAME, soapObject.getPropertyAsString("version"), "Floorplan Information Version");
		}
		db.close();
		return result;
	}
	
	@Override
	public void run() {
		// set warehouse ID as web service input
		setWSInputs();

		try {
			//TODO: uncomment the next
			if(!checkFloorplanVersion(webService.invokeMethod("checkVersion", wsArguments))) {
				getAPsFromSoap(webService.invokeMethod("APsLocation", wsArguments));
				getCategoriesFromSoap(webService.invokeMethod("SectionsLocation", wsArguments));
			}
			//TODO: comment the next part
//			if(!checkFloorplanVersion(null)) {
//				getAPsFromSoap(null);
//				getCategoriesFromSoap(null);
//			}
//		} catch (ConnectException e) {
//			Log.d(TAG, e.getMessage());
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	
}
