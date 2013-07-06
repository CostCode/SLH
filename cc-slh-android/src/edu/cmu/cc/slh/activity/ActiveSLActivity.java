/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import java.util.HashMap;
import java.util.Map;

import edu.cmu.cc.android.activity.async.AbstractAsyncListActivity;
import edu.cmu.cc.slh.R;
import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 5, 2013
 */
@SuppressLint("UseSparseArrays")
public class ActiveSLActivity extends AbstractAsyncListActivity {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Map<Integer, MenuItem> menuItems;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menuItems = new HashMap<Integer, MenuItem>(3);
		
		menuItems.put(R.string.shoppinglist_item_add, 
				menu.add(R.string.shoppinglist_item_add).setIcon(R.drawable.add));
		menuItems.put(R.string.settings_triangulation_onOff, 
				menu.add(R.string.settings_triangulation_onOff).setIcon(R.drawable.add));
		menuItems.put(R.string.shoppinglist_show_summary, 
				menu.add(R.string.shoppinglist_show_summary).setIcon(R.drawable.add));
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		setMenuItemState(R.string.shoppinglist_item_add, true, true);
		setMenuItemState(R.string.settings_triangulation_onOff, true, true);
		setMenuItemState(R.string.shoppinglist_show_summary, true, true);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getTitle().equals(
				getString(R.string.shoppinglist_item_add))) {
			//TODO:
		} else if (item.getTitle().equals(
				getString(R.string.settings_triangulation_onOff))) {
			//TODO:
		} else if (item.getTitle().equals(
				getString(R.string.shoppinglist_show_summary))) {
			//TODO:
		}
		
		return true;
	}
	
	@Override
	public void onAsyncTaskFailed(Class<?> taskClass, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void setMenuItemState(int menuItemTitleResID, 
			boolean visible, boolean enabled) {
		
		MenuItem menuItem = menuItems.get(menuItemTitleResID);
		menuItem.setVisible(visible);
		menuItem.setEnabled(enabled);
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM 
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

}
