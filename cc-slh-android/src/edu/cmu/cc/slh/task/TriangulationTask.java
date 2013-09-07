/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.SettingsAdapter;
import edu.cmu.cc.slh.model.AccessPoint;
import edu.cmu.cc.slh.model.Section;
import edu.cmu.cc.slh.service.proximityalert.ProximityIntentReceiver;
import edu.cmu.cc.slh.service.proximityalert.triangulation.Triangulation;
import edu.cmu.cc.slh.service.proximityalert.triangulation.WCL;
import edu.cmu.cc.slh.task.FetchFloorPlanTask.IFetchFloorPlanTaskCaller;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 *  DESCRIPTION: 
 *	
 *  @author Nohsam Park, Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 23, 2013
 */
public class TriangulationTask extends AsyncTask<Void, Void, Void> 
implements IFetchFloorPlanTaskCaller {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final long INTERVAL = 5000;

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private WifiManager wifiManager;
	
	private Map<String, Object> initParams;
	
	private Triangulation triangulation;
	
	
	private List<Section> sections;
	
	private List<AccessPoint> accessPoints;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public TriangulationTask() {
		
		ctx = ApplicationState.getContext();
		
		wifiManager = (WifiManager) 
				ctx.getSystemService(Context.WIFI_SERVICE);
		
		initParams = new HashMap<String, Object>();
		
		if (!initPreference()) {
			throw new IllegalStateException("WiFi is not enabled");
		}
		
		if (wifiManager.isWifiEnabled() == false) {
			wifiManager.setWifiEnabled(true);
		}
	}

	//-------------------------------------------------------------------------
	// PROTECTED METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		Toast.makeText(ctx, R.string.proximityalert_started, 
				Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			
			//---------------------------------------------------
			// Fetching Sections and AccessPoints for the selected
			// (default) warehouse from the WebService.
			//---------------------------------------------------
			
			fetchWarehouseFloorPlan();
			
			//---------------------------------------------------
			// Finding nearest AccessPoint and Section
			// and firing ProximityAlert event.
			//---------------------------------------------------
			
			triangulation = new WCL(wifiManager, initParams);
			
			while (true) {
				
				if (!SettingsAdapter.retrieveProximityAlertEnabled() 
						|| isCancelled()) {
					break;
				}
				
				AccessPoint nearestAP = triangulation.findNearestAccessPoint(accessPoints);
				if (nearestAP != null) {
					Section nearestSection = 
							triangulation.findNearestSection(sections, 
									nearestAP.getPosX(), nearestAP.getPosY());
				
					if (nearestSection != null) {
						Intent proximityIntent = new Intent(
								ProximityIntentReceiver.EVENT_PROXIMITY_ALERT);
						
						ApplicationState.getInstance().setNearestAP(nearestAP);
						ApplicationState.getInstance()
								.setNearestSection(nearestSection);
						
						LocalBroadcastManager.getInstance(ctx)
								.sendBroadcast(proximityIntent);
					}
					
					try {
						Thread.sleep(INTERVAL);
					} catch (InterruptedException e) {
						Logger.logError(getClass(), e);
					}
					
				}
			}
			
		} catch (Throwable t) {
			final String errMessage = 
					ctx.getString(R.string.proximityalert_error, 
							t.getMessage());
			Logger.logErrorAndAlert(ctx, getClass(), 
					StringUtils.getLimitedString(errMessage, 200, "..."));
		} finally {
			if (triangulation != null) {
				triangulation.stop();
				triangulation = null;
			}
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if (triangulation != null) {
			triangulation.stop();
		}
		
		Toast.makeText(ctx, R.string.proximityalert_stopped, 
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	//-------------------------------------------------------------------------
	// IFetchFloorPlanTaskCaller METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void onFetchFloorPlanTaskSucceeded(List<Section> sections,
			List<AccessPoint> accessPoints) {
		
		if (sections == null || sections.size() == 0) {
			throw new IllegalArgumentException("Sections list is empty or null");
		}
		
		if (accessPoints == null || accessPoints.size() == 0) {
			throw new IllegalArgumentException("AccessPoints list is empty or null");
		}
		
		this.sections = sections;
		this.accessPoints = accessPoints;
	}

	@Override
	public void onFetchFloorPlanTaskFailed(Throwable t) {
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private boolean initPreference() {
		
		initParams.put(Triangulation.TRIANG_METHOD, 
				Triangulation.TRIANG_METHOD_WCL);
		initParams.put(Triangulation.SCAN_NUMBER, 
				Triangulation.DEFAULT_SCAN_NUMBER);
		initParams.put(Triangulation.NOISE_FILTER, 
				Triangulation.DEFAULT_NOISE_FILTER);
		
		return true;
	}
	
	private void fetchWarehouseFloorPlan() {
		
		FetchFloorPlanTask floorPlanTask = 
				new FetchFloorPlanTask(ctx, this);
		
		floorPlanTask.execute();
	}
	
}
