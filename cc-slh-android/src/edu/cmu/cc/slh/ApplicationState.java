/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh;

import java.util.List;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.model.ShoppingList;
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
	
	private ShoppingList shoppingList;
	
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

	public ShoppingList getShoppingList() {
		return shoppingList;
	}
	public void setShoppingList(ShoppingList shoppingList) {
		this.shoppingList = shoppingList;
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