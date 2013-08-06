/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh;

import java.util.List;
import java.util.Map;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.activity.ITabHostActivity;
import edu.cmu.cc.slh.model.AccessPoint;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.Section;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;
import edu.cmu.cc.slh.model.Warehouse;
import edu.cmu.cc.slh.service.proximityalert.ProximityIntentReceiver;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;


/**
 *  DESCRIPTION: This class is used to hold application data.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 10, 2013
 */
public class ApplicationState extends Application {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private static ApplicationState instance;
	
	private ProximityIntentReceiver proximityReceiver;
	
	
	private ITabHostActivity tabHostActivity;
	
	
	private String memberId;
	
	private Map<Long,ItemCategory> categories;
	
	private List<Warehouse> warehouses;
	
	private List<ShoppingList> shoppingLists;
	
	private ShoppingList currentSL;
	
	private ShoppingListItem currentSLItem;
	
	
	private Section nearestSection;
	
	private AccessPoint nearestAP;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ApplicationState() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	public static ApplicationState getInstance() {
		return instance;
	}
	
	public static Context getContext() {
		return instance.getApplicationContext();
	}
	
	public ITabHostActivity getTabHostActivity() {
		return tabHostActivity;
	}
	public void setTabHostActivity(ITabHostActivity tabHostActivity) {
		this.tabHostActivity = tabHostActivity;
	}

	public String getMemberId() {
		return memberId;
	}
	public synchronized void setMemberId(String memberId) {
		this.memberId = memberId;
	}	
	
	public Map<Long,ItemCategory> getCategories() {
		return categories;
	}
	public synchronized void setCategories(Map<Long,ItemCategory> categories) {
		this.categories = categories;
	}
	
	public List<Warehouse> getWarehouses() {
		return warehouses;
	}
	public synchronized void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public List<ShoppingList> getShoppingLists() {
		return shoppingLists;
	}
	public synchronized void setShoppingLists(List<ShoppingList> shoppingLists) {
		this.shoppingLists = shoppingLists;
	}

	public ShoppingList getCurrentSL() {
		return currentSL;
	}
	public synchronized void setCurrentSL(ShoppingList currentSL) {
		this.currentSL = currentSL;
	}
	
	public ShoppingListItem getCurrentSLItem() {
		return currentSLItem;
	}
	public synchronized void setCurrentSLItem(ShoppingListItem currentSLItem) {
		this.currentSLItem = currentSLItem;
	}
	
	public Section getNearestSection() {
		return nearestSection;
	}
	public synchronized void setNearestSection(Section nearestSection) {
		this.nearestSection = nearestSection;
	}

	public AccessPoint getNearestAP() {
		return nearestAP;
	}
	public synchronized void setNearestAP(AccessPoint nearestAP) {
		this.nearestAP = nearestAP;
	}
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	@Override
	public void onCreate() {
		super.onCreate();
		
		Logger.logDebug(getClass(), "SLH application has started...");
		
		instance = this;
		
		proximityReceiver = new ProximityIntentReceiver();
		registerProximityReceiver(proximityReceiver);
	}

	@Override
	public void onTerminate() {
		
		unregisterProximityReceiver(proximityReceiver);
		
		Logger.logDebug(getClass(), "SLH application has stopped...");
		
		super.onTerminate();
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void registerProximityReceiver(ProximityIntentReceiver receiver) {
		
		if (receiver != null) {
			Logger.logDebug(getClass(), 
					"Registering ProximityIntentReceiver...");
			
			IntentFilter proximityIntentFilter = new IntentFilter(
					ProximityIntentReceiver.EVENT_PROXIMITY_ALERT);
			
			LocalBroadcastManager.getInstance(instance.getApplicationContext())
					.registerReceiver(receiver, proximityIntentFilter);
		}
	}
	
	private void unregisterProximityReceiver(ProximityIntentReceiver receiver) {
		
		if (receiver != null) {
			Logger.logDebug(getClass(), 
					"Unregistering ProximityIntentReceiver...");
			
			LocalBroadcastManager.getInstance(instance.getApplicationContext())
					.unregisterReceiver(receiver);
		}
	}

}