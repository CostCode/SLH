/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import edu.cmu.cc.android.activity.async.AbstractAsyncListActivity;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.dialog.ShoppingListDialog;
import edu.cmu.cc.slh.dialog.ShoppingListDialog.IShoppingListDialogCaller;
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
@SuppressLint("UseSparseArrays")
public class ShoppingListsActivity extends AbstractAsyncListActivity
implements IFetchShoppingListsTaskCaller, IShoppingListDialogCaller {

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
	public ShoppingListsViewAdapter getListAdapter() {
		return (ShoppingListsViewAdapter) super.getListAdapter();
	}

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
	public void onFetchShoppingListsTaskSucceeded(List<ShoppingList> list) {
		
		ApplicationState.getInstance().setShoppingLists(list);
		
		refreshGUI();
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getTitle().equals(getString(R.string.shoppinglist_all_add))) {
			ShoppingList newSL = new ShoppingList(
					null, new Date(System.currentTimeMillis()), null);
			showShoppingListDialog(newSL);
		}
		
		return true;
	}
	
	@Override
	public void onShoppingListSaved() {
		
		Runnable callback = new Runnable() {
			
			@Override
			public void run() {
				fetchShoppingLists();
			}
		};
		
		Message osMessage = Message.obtain(this.asyncTaskHandler, callback);
		osMessage.sendToTarget();
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
	
	/**
	 * Displays a detail shopping list dialog
	 */
	private void showShoppingListDialog(ShoppingList sl) {
		
		ApplicationState.getInstance().setShoppingList(sl);
		
		DialogFragment slDialog = ShoppingListDialog.newInstance(this, this);
		slDialog.show(getFragmentManager(), null);
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
		
		return StringUtils.getLimitedString(
				getString(msgResID, t.getMessage()), 200, "...");
	}

	private void fetchShoppingLists() {
		new FetchShoppingListsTask(this, this).execute();
	}

	/**
	 * Setting the shopping lists adapter
	 */
	private void setListAdapter() {
		
		List<ShoppingList> list = 
				ApplicationState.getInstance().getShoppingLists();
		
		setListAdapter(new ShoppingListsViewAdapter(this, list));
	}
	
	/**
	 * Initializes list click capability
	 */
	private void prepareListClick() {
		
		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				
				ShoppingList sl = 
						(ShoppingList) getListAdapter().getItem(position);
				
				showShoppingListDialog(sl);
			}
		});
	}
	
	/*
	 * Update activity UI
	 */
	private void refreshGUI() {
		setListAdapter();
		onContentChanged();
		prepareListClick();
	}
	
}