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
 *  DESCRIPTION: 
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
	
	public void execute() {
		
		try {
			
			onPreExecute();
			
			//---------------------------------------------------
			// Getting User Membership ID
			//---------------------------------------------------
			
			memberId = ActivationAdapter.retrieveMemberId();
			if (StringUtils.isNullOrEmpty(memberId)) {
				throw new RuntimeException("MemberID is null or empty!");
			}
			
			//---------------------------------------------------
			// Getting Default Warehouse
			//---------------------------------------------------
			
			Warehouse wh = 
					ApplicationState.getInstance().getDefaultWarehouse();
			if (wh == null) {
				throw new RuntimeException("Default Warehouse is null");
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
	
	private void onPreExecute() {
		
		if (!DeviceUtils.isNetworkConnectedElseAlert(
				ctx, getClass(), R.string.ws_error_noconnection)) {
			throw new RuntimeException("Network is not available...");
		}
		
		sectionDAO = new SectionDAO();
		accessPointDAO = new AccessPointDAO();
		warehouseDAO = new WarehouseDAO();
		
		errorState = false;
	}

	private void onPostExecute() {
		sectionDAO.close();
		accessPointDAO.close();
		warehouseDAO.close();
		
		if (!errorState) {
			caller.onFetchFloorPlanTaskSucceeded(sections, accessPoints);
		}
	}

	private int retrieveLocalWarehouseVersion(Warehouse wh) {
		return wh.getVersion();
	}
	
	private int retrieveServerWarehouseVersion(Warehouse wh) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(),String.format("Retrieving Version " +
				"number for Warehouse [%s]", wh));
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		Map<String, String> args = SecureWSHelper.initWSArguments(ctx, memberId);
		args.put(ctx.getString(
				R.string.ws_warehouse_property_id), String.valueOf(wh.getId()));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_warehouse_method_retrieveVersion), 
				args);
		
		String strVersion = response.getPropertyAsString(ctx
				.getString(R.string.ws_warehouse_property_version));
		
		Logger.logDebug(this.getClass(), 
				String.format("Server Warehouse Version: [%s]", strVersion));
		
		return Integer.parseInt(strVersion);
	}
	
	private void deleteLocalWarehouseData(Warehouse wh) {
		sectionDAO.deleteAll(wh);
		accessPointDAO.deleteAll(wh);
	}
	
	//-------------------------------------------------------------------------
	// SECTIONS METHODS
	//-------------------------------------------------------------------------
	
	private void retrieveAndSaveServerSections(Warehouse wh) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving Sections from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		Map<String, String> args = SecureWSHelper.initWSArguments(ctx, memberId);
		args.put(ctx.getString(
				R.string.ws_warehouse_property_id), String.valueOf(wh.getId()));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_section_method_retrieveSections), 
				args);
		
		parseAndSaveSectionsXML(response, wh);
	}
	
	private void parseAndSaveSectionsXML(SoapObject root, Warehouse wh) {
		
		if (root == null) {
			throw new RuntimeException("Soap source is null");
		}
		
		Object sectionsProperty = root.getProperty(
				ctx.getString(R.string.ws_section_property_sections));
		
		if (sectionsProperty instanceof SoapObject) {
			SoapObject sectionsXml = (SoapObject) sectionsProperty;
			
			for (int i = 0; i < sectionsXml.getPropertyCount(); i++) {
				Object sectionProperty = sectionsXml.getProperty(i);
				
				if (sectionProperty instanceof SoapObject) {
					SoapObject sectionXml = (SoapObject) sectionProperty;
					
					Section section = new Section();
					section.setWarehouse(wh);
					section.setId(SoapUtils.getLongPropertyValue(sectionXml, 
							ctx.getString(R.string.ws_section_property_id)));
					section.setPosX(SoapUtils.getDoublePropertyValue(sectionXml, 
							ctx.getString(R.string.ws_section_property_posx)));
					section.setPosY(SoapUtils.getDoublePropertyValue(sectionXml, 
							ctx.getString(R.string.ws_section_property_posy)));
					
					saveSection(section);
				}
			}
		}
		
	}
	
	private void saveSection(Section section) {
		sectionDAO.save(section);
	}
	
	//-------------------------------------------------------------------------
	// ACCESS POINT METHODS
	//-------------------------------------------------------------------------
	
	private void retrieveAndSaveServerAccessPoints(Warehouse wh) 
			throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving AccessPoints from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_warehouse_namespace), 
				ctx.getString(R.string.ws_warehouse_url));
		
		Map<String, String> args = SecureWSHelper.initWSArguments(ctx, memberId);
		args.put(ctx.getString(
				R.string.ws_warehouse_property_id), String.valueOf(wh.getId()));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_ap_method_retrieveAccessPoints), 
				args);
		
		parseAndSaveAPsXML(response, wh);
	}
	
	private void parseAndSaveAPsXML(SoapObject root, Warehouse wh) {
		
		if (root == null) {
			throw new RuntimeException("Soap source is null");
		}
		
		Object apsProperty = root.getProperty(
				ctx.getString(R.string.ws_ap_property_accesspoints));
		
		if (apsProperty instanceof SoapObject) {
			SoapObject apsXml = (SoapObject) apsProperty;
			
			for (int i = 0; i < apsXml.getPropertyCount(); i++) {
				Object apProperty = apsXml.getProperty(i);
				
				if (apProperty instanceof SoapObject) {
					SoapObject apXml = (SoapObject) apProperty;
					
					AccessPoint ap = new AccessPoint();
					
					ap.setWarehouse(wh);
					ap.setId(SoapUtils.getLongPropertyValue(apXml, 
							ctx.getString(R.string.ws_ap_property_id)));
					ap.setSsid(apXml.getPropertyAsString(
							ctx.getString(R.string.ws_ap_property_ssid)));
					ap.setPosX(SoapUtils.getDoublePropertyValue(apXml, 
							ctx.getString(R.string.ws_ap_property_posx)));
					ap.setPosY(SoapUtils.getDoublePropertyValue(apXml, 
							ctx.getString(R.string.ws_ap_property_posy)));
					
					saveAccessPoint(ap);
				}
			}
		}
	}
	
	private void saveAccessPoint(AccessPoint ap) {
		accessPointDAO.save(ap);
	}
	
	
	
	private void saveLocalWarehouseVersion(Warehouse wh, int version) {
		wh.setVersion(version);
		warehouseDAO.save(wh);
		ApplicationState.getInstance().setDefaultWarehouse(wh);
	}
	
	private List<Section> fetchLocalSections(Warehouse wh) {
		return sectionDAO.getAll(wh);
	}
	
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
