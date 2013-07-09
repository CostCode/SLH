/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cmu.cc.android.activity.async.AbstractAsyncListActivity;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActiveSLAdapter;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;
import edu.cmu.cc.slh.task.FetchSLItemsTask;
import edu.cmu.cc.slh.task.FetchSLItemsTask.IFetchSLItemsTaskCaller;
import edu.cmu.cc.slh.view.adapter.ActiveSLViewListAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

/**
 *  DESCRIPTION: Active shopping list activity
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 5, 2013
 */
@SuppressLint("UseSparseArrays")
public class ActiveSLActivity extends AbstractAsyncListActivity
implements IFetchSLItemsTaskCaller {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Handler asyncTaskHandler;
	
	private Map<Integer, MenuItem> menuItems;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	@Override
	public ActiveSLViewListAdapter getListAdapter() {
		return (ActiveSLViewListAdapter) super.getListAdapter();
	}
	

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_sl);
		
		ShoppingList activeSL = ActiveSLAdapter.retrieveActiveShoppingList();
		ApplicationState.getInstance().setActiveSL(activeSL);
		if (activeSL != null) {
			fetchShoppingListItems(activeSL);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		asyncTaskHandler = new Handler();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menuItems = new HashMap<Integer, MenuItem>(3);
		
		menuItems.put(R.string.sl_item_add, 
				menu.add(R.string.sl_item_add).setIcon(R.drawable.add));
		menuItems.put(R.string.settings_triangulation_onOff, 
				menu.add(R.string.settings_triangulation_onOff).setIcon(R.drawable.add));
		menuItems.put(R.string.sl_show_summary, 
				menu.add(R.string.sl_show_summary).setIcon(R.drawable.add));
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		setMenuItemState(R.string.sl_item_add, true, true);
		setMenuItemState(R.string.settings_triangulation_onOff, true, true);
		setMenuItemState(R.string.sl_show_summary, true, true);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getTitle().equals(
				getString(R.string.sl_item_add))) {
			//TODO:
		} else if (item.getTitle().equals(
				getString(R.string.settings_triangulation_onOff))) {
			//TODO:
		} else if (item.getTitle().equals(
				getString(R.string.sl_show_summary))) {
			//TODO:
		}
		
		return true;
	}
	
	@Override
	public void onAsyncTaskFailed(Class<?> taskClass, Throwable t) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onFetchSLItemsTaskSucceeded(List<ShoppingListItem> items) {
		
		ShoppingList activeSL = ApplicationState.getInstance().getActiveSL();
		if (activeSL != null) {
			activeSL.setItems(items);
		}
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
	
	private void fetchShoppingListItems(ShoppingList sl) {
		new FetchSLItemsTask(this).execute(new ShoppingList[]{sl});
	}
	
	private void setListAdapter() {
		
		ActiveSLViewListAdapter adapter = new ActiveSLViewListAdapter(this);
		
		List<ItemCategory> categories = 
				ApplicationState.getInstance().getCategories();
		
		for (ItemCategory category : categories) {
			adapter.addCategory(categoryName, categoryAdapter);
		}
		
	}
	
	private List<ShoppingListItem> getSLItemsByCategory(ItemCategory category) {
		
		ArrayAdapter<ShoppingListItem> adapter = 
				new ArrayAdapter<ShoppingListItem>(this, 
						R.layout.active_sl_row_item, textViewResourceId); 
		
		for () {
			
		}
		
	}
	
}
