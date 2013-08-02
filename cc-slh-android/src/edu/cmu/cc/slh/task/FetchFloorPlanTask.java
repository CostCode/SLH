/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.service.soap.util.SoapUtils;
import edu.cmu.cc.android.util.DeviceUtils;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActivationAdapter;
import edu.cmu.cc.slh.dao.AccessPointDAO;
import edu.cmu.cc.slh.dao.SectionDAO;
import edu.cmu.cc.slh.dao.WarehouseDAO;
import edu.cmu.cc.slh.model.AccessPoint;
import edu.cmu.cc.slh.model.Section;
import edu.cmu.cc.slh.model.Warehouse;
import android.content.Context;


/**
 *  This task is used for downloading or fetching from the local DB the 
 *  list of warehouse Sections and Access points.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class FetchFloorPlanTask {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private IFetchFloorPlanTaskCaller caller;
	
	private SectionDAO sectionDAO;
	
	private AccessPointDAO accessPointDAO;
	
	private WarehouseDAO warehouseDAO;
	
	private String memberId;
	
	private List<Section> sections;
	
	private List<AccessPoint> accessPoints;
	
	private boolean errorState;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public FetchFloorPlanTask(Context ctx, IFetchFloorPlanTaskCaller caller) {
		super();
		
		this.ctx = ctx;
		this.caller = caller;
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Main task execution method.
	 */
	public void execute() {
		
		try {
			
			onPreExecute();
			
			//---------------------------------------------------
			// Getting User Membership ID
			//---------------------------------------------------
			
			memberId = ActivationAdapter.retrieveMemberId();
			if (StringUtils.isNullOrEmpty(memberId)) {
				throw new IllegalArgumentException("MemberID is null or empty!");
			}
			
			//---------------------------------------------------
			// Getting Default Warehouse
			//---------------------------------------------------
			
			Warehouse wh = 
					ApplicationState.getInstance().getDefaultWarehouse();
			if (wh == null) {
				throw new IllegalArgumentException("Default Warehouse is null");
			}
			
			//---------------------------------------------------
			// Warehouse version checking and fetching
			//---------------------------------------------------
			
			int localWarehouseVersion = retrieveLocalWarehouseVersion(wh);
			int serverWarehouseVersion = retrieveServerWarehouseVersion(wh);
			
			if (localWarehouseVersion != serverWarehouseVersion) {
				deleteLocalWarehouseData(wh);
				retrieveAndSaveServerSections(wh);
				retrieveAndSaveServerAccessPoints(wh);
				saveLocalWarehouseVersion(wh, serverWarehouseVersion);
			}
			
			sections = fetchLocalSections(wh);
			accessPoints = fetchLocalAccessPoints(wh);
			
		} catch (Throwable t) {
			errorState = true;
			caller.onFetchFloorPlanTaskFailed(t);
		}
		
		onPostExecute();
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Task pre execution setup and check
	 */
	private void onPreExecute() {
		
		if (!DeviceUtils.isNetworkConnectedElseAlert(
				ctx, getClass(), R.string.ws_error_noconnection)) {
			throw new IllegalStateException("Network is not available...");
		}
		
		sectionDAO = new SectionDAO();
		accessPointDAO = new AccessPointDAO();
		warehouseDAO = new WarehouseDAO();
		
		errorState = false;
	}

	/**
	 * Task post execution activities
	 */
	private void onPostExecute() {
		sectionDAO.close();
		accessPointDAO.close();
		warehouseDAO.close();
		
		if (!errorState) {
			caller.onFetchFloorPlanTaskSucceeded(sections, accessPoints);
		}
	}

	/**
	 * Gets the version number of the given Warehouse object
	 * 
	 * @param wh - Warehouse object
	 * @return warehouse version number
	 */
	private int retrieveLocalWarehouseVersion(Warehouse wh) {
		return wh.getVersion();
	}
	
	/**
	 * Calls the web service to get the version number for the
	 * given warehouse id.
	 * 
	 * @param wh - Warehouse object
	 * @return warehouse version number
	 * @throws Throwable - web service call exceptions
	 */
	private int retrieveServerWarehouseVersion(Warehouse wh) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(),String.format("Retrieving Server " +
				"Version number for Warehouse [%s]", wh));
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		Map<String, String> args = SecureWSHelper.initWSArguments(ctx, memberId);
		args.put(ctx.getString(R.string.ws_warehouse_property_id), 
				String.valueOf(wh.getId()));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_warehouse_method_retrieveVersion), 
				args);
		
		return parseWarehouseVersion(response);
	}
	
	/**
	 * Parses warehouse version number from the SoapObject
	 * 
	 * @param root - SoapObject
	 * @return server warehouse version number
	 */
	private int parseWarehouseVersion(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		if (SoapUtils.hasException(ctx, result)) {
			throw new IllegalStateException(
					SoapUtils.getException(ctx, result));
		}
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Removes all the sections and access point data for the
	 * given warehouse from the local DB.
	 * 
	 * @param wh - Warehouse object
	 */
	private void deleteLocalWarehouseData(Warehouse wh) {
		sectionDAO.deleteAll(wh);
		accessPointDAO.deleteAll(wh);
	}
	
	//-------------------------------------------------------------------------
	// SECTIONS METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Calls the web service to get the list of sections for the given
	 * warehouse. After, these sections are saved in the local DB.
	 * 
	 * @param wh - Warehouse object
	 * @throws Throwable - web service call exceptions
	 */
	private void retrieveAndSaveServerSections(Warehouse wh) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving Sections from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		Map<String, String> args = SecureWSHelper.initWSArguments(ctx, memberId);
		args.put(ctx.getString(R.string.ws_warehouse_property_id), 
				String.valueOf(wh.getId()));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_section_method_retrieveSections), 
				args);
		
		parseAndSaveSectionsXML(response, wh);
	}
	
	/**
	 * Parses sections from the Soap object and save them in the local DB.
	 * 
	 * @param root - SoapObject
	 * @param wh - Warehouse object
	 */
	private void parseAndSaveSectionsXML(SoapObject root, Warehouse wh) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		if (SoapUtils.hasException(ctx, result)) {
			throw new IllegalStateException(
					SoapUtils.getException(ctx, result));
		}
		
		for (int i = 0; i < result.getPropertyCount(); i++) {
			SoapObject sectionProperty = 
					(SoapObject) result.getProperty(i);
			
			Section section = new Section();
			section.setWarehouse(wh);
			section.setId(SoapUtils.getLongPropertyValue(sectionProperty, 
					ctx.getString(R.string.ws_section_property_id)));
			section.setPosX(SoapUtils.getDoublePropertyValue(sectionProperty, 
					ctx.getString(R.string.ws_section_property_posx)));
			section.setPosY(SoapUtils.getDoublePropertyValue(sectionProperty, 
					ctx.getString(R.string.ws_section_property_posy)));
			
			saveSection(section);
		}
		
	}
	
	/**
	 * Saves the give Section object in the local DB
	 * 
	 * @param section - Section object
	 */
	private void saveSection(Section section) {
		sectionDAO.save(section);
	}
	
	//-------------------------------------------------------------------------
	// ACCESS POINT METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Calls the web service to get the list of access points for the
	 * given warehouse
	 * 
	 * @param wh - Warehouse object
	 * @throws Throwable - web service call exceptions
	 */
	private void retrieveAndSaveServerAccessPoints(Warehouse wh) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving AccessPoints from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		Map<String, String> args = SecureWSHelper.initWSArguments(ctx, memberId);
		args.put(ctx.getString(R.string.ws_warehouse_property_id), 
				String.valueOf(wh.getId()));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_ap_method_retrieveAccessPoints), 
				args);
		
		parseAndSaveAPsXML(response, wh);
	}
	
	/**
	 * Parses access points from the soap object and saves them in the
	 * local DB
	 * 
	 * @param root - SoapObject
	 * @param wh - Warehouse object
	 */
	private void parseAndSaveAPsXML(SoapObject root, Warehouse wh) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		if (SoapUtils.hasException(ctx, result)) {
			throw new IllegalStateException(
					SoapUtils.getException(ctx, result));
		}
		
		for (int i = 0; i < result.getPropertyCount(); i++) {
			SoapObject apProperty = (SoapObject) result.getProperty(i);
			
			AccessPoint ap = new AccessPoint();
			
			ap.setWarehouse(wh);
			ap.setId(SoapUtils.getLongPropertyValue(apProperty, 
					ctx.getString(R.string.ws_ap_property_id)));
			ap.setSsid(apProperty.getPropertyAsString(
					ctx.getString(R.string.ws_ap_property_ssid)));
			ap.setPosX(SoapUtils.getDoublePropertyValue(apProperty, 
					ctx.getString(R.string.ws_ap_property_posx)));
			ap.setPosY(SoapUtils.getDoublePropertyValue(apProperty, 
					ctx.getString(R.string.ws_ap_property_posy)));
			
			saveAccessPoint(ap);
		}
		
	}
	
	/**
	 * Saves the given AccessPoint object in the local DB
	 * 
	 * @param ap - AccessPoint object
	 */
	private void saveAccessPoint(AccessPoint ap) {
		accessPointDAO.save(ap);
	}
	
	/**
	 * Updates the version of the selected warehouse
	 * 
	 * @param wh - Selected warehouse
	 * @param version - new version
	 */
	private void saveLocalWarehouseVersion(Warehouse wh, int version) {
		wh.setVersion(version);
		warehouseDAO.save(wh);
		ApplicationState.getInstance().setDefaultWarehouse(wh);
	}
	
	/**
	 * Loads Section objects for the given warehouse
	 * 
	 * @param wh - Warehouse object
	 * @return list of sections
	 */
	private List<Section> fetchLocalSections(Warehouse wh) {
		return sectionDAO.getAll(wh);
	}
	
	/**
	 * Loads AccessPoint objects for the given warehouse
	 * 
	 * @param wh - Warehouse object
	 * @return list of access points
	 */
	private List<AccessPoint> fetchLocalAccessPoints(Warehouse wh) {
		return accessPointDAO.getAll(wh);
	}
	
	//-------------------------------------------------------------------------
	// INTERFACE
	//-------------------------------------------------------------------------
	
	/**
	 * All the callers of FetchFloorPlanTask class should implement this 
	 * interface.
	 */
	public interface IFetchFloorPlanTaskCaller {
		
		/**
		 * Once this task completes, it will return the list of sections and
		 * access points for the selected warehouse.
		 * 
		 * @param sections - selected warehouse sections
		 * @param accessPoints - selected warehouse access points
		 */
		public void onFetchFloorPlanTaskSucceeded(
				List<Section> sections, List<AccessPoint> accessPoints);
		
		/**
		 * If the fetching task fails, it will inform the caller by calling
		 * this method and passing error object
		 * 
		 * @param t - error object
		 */
		public void onFetchFloorPlanTaskFailed(Throwable t);
		
	}
	
}
