/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh;

import java.util.List;
import java.util.Map;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;
import android.app.Application;
import android.content.Context;


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
	
	private List<ShoppingList> shoppingLists;
	
	private ShoppingList currentSL;
	
	private Map<Long,ItemCategory> categories;
	
	private ShoppingListItem currentSLItem;
	
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
	
	public List<ShoppingList> getShoppingLists() {
		return shoppingLists;
	}
	public void setShoppingLists(List<ShoppingList> shoppingLists) {
		this.shoppingLists = shoppingLists;
	}

	public ShoppingList getCurrentSL() {
		return currentSL;
	}
	public void setCurrentSL(ShoppingList currentSL) {
		this.currentSL = currentSL;
	}
	
	public Map<Long,ItemCategory> getCategories() {
		return categories;
	}
	public void setCategories(Map<Long,ItemCategory> categories) {
		this.categories = categories;
	}
	
	public ShoppingListItem getCurrentSLItem() {
		return currentSLItem;
	}
	public void setCurrentSLItem(ShoppingListItem currentSLItem) {
		this.currentSLItem = currentSLItem;
	}
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	@Override
	public void onCreate() {
		super.onCreate();
		
		Logger.logDebug(getClass(), "SLH application was created...");
		instance = this;
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}