/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import edu.cmu.cc.android.activity.async.AbstractAsyncListActivity;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.task.FetchShoppingListsTask;
import edu.cmu.cc.slh.task.FetchShoppingListsTask.IFetchShoppingListsTaskCaller;
import edu.cmu.cc.slh.view.adapter.ShoppingListsViewAdapter;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
public class ShoppingListsActivity extends AbstractAsyncListActivity
implements IFetchShoppingListsTaskCaller {

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

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allshoppinglists);
		
		fetchShoppingLists();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		asyncTaskHandler = new Handler();
	}
	
	@Override
	public ShoppingListsViewAdapter getListAdapter() {
		return (ShoppingListsViewAdapter) super.getListAdapter();
	}

	@Override
	public void onFetchShoppingListsTaskSucceeded(List<ShoppingList> list) {
		
		setListAdapter(list);
	}
	
	@Override
	public void onAsyncTaskFailed(Class<?> taskClass, final Throwable t) {
		
		final String errorMsg = getAsyncTaskFailedMessage(taskClass, t);
		
		Runnable callback = new Runnable() {
			
			@Override
			public void run() {
				
				DialogInterface.OnClickListener dialogListener =
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, 
									int which) {
								ShoppingListsActivity.this.finish();
							}
						};
				
				Logger.logErrorAndAlert(ShoppingListsActivity.this, 
						ShoppingListsActivity.class, errorMsg, t, dialogListener);
			}
		};
		
		Message osMessage = Message.obtain(this.asyncTaskHandler, callback);
		osMessage.sendToTarget();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menuItems = new HashMap<Integer, MenuItem>(1);
		
		menuItems.put(R.string.shoppinglist_all_add, 
				menu.add(R.string.shoppinglist_all_add).setIcon(R.drawable.add));
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		setMenuItemState(R.string.shoppinglist_all_add, true, true);
		
		return true;
	}
	

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void setMenuItemState(int menuItemTitleResID, 
			boolean visible, boolean enabled) {
		
		MenuItem menuItem = menuItems.get(menuItemTitleResID);
		menuItem.setVisible(visible);
		menuItem.setEnabled(enabled);
	}

	private void setListAdapter(List<ShoppingList> list) {
		setListAdapter(new ShoppingListsViewAdapter(this, list));
	}
	
	private void fetchShoppingLists() {
		boolean fetched = (getListAdapter() != null);
		if (!fetched) {
			new FetchShoppingListsTask(this, this).execute();
		}
	}
	
	private String getAsyncTaskFailedMessage(Class<?> taskClass, Throwable t) {
		
		int msgResID = R.string.error_unspecified;
		
		if (taskClass == FetchShoppingListsTask.class) {
			msgResID = R.string.shoppinglist_all_error_fetch;
		} else {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException("Unexpected class: " 
							+ taskClass.toString()));
		}
		
		return StringUtils.limitLength(
				getString(msgResID, t.getMessage()), 200, "...");
	}

}