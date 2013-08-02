/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import android.view.Menu;
import android.view.MenuItem;

/**
 *  All the activities which are presented as tabs should implement
 *  this interface for decorating and handling the menu items in the
 *  main tab holder activity 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 12, 2013
 */
public interface ITabActivity {

	/**
	 * Sets up the parent tab host activity for this tab activity
	 * 
	 * @param tabHost - parent tab host
	 */
	public void init(ITabHostActivity tabHost);
	
	/**
	 * Populates the given menu with menu items
	 * 
	 * @param menu - root menu
	 * @return true - show the menu, false - hide
	 */
	public boolean prepareOptionsMenu(Menu menu);
	
	/**
	 * Handles the user selection of the menu item
	 * 
	 * @param item - menu item was selected
	 * @return true - show the menu, false - hide
	 */
	public boolean handleOptionsMenuItemSelection(MenuItem item);
	
	/**
	 * Refreshes the UI of the activity
	 */
	public void refresh();
	
}
