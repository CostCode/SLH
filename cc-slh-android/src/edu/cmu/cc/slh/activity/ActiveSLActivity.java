/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cmu.cc.android.activity.async.AbstractAsyncListActivity;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.dao.SLItemDAO;
import edu.cmu.cc.slh.dialog.SLItemDialog;
import edu.cmu.cc.slh.dialog.SLItemDialog.ISLItemDialogCaller;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;
import edu.cmu.cc.slh.task.FetchSLItemsTask;
import edu.cmu.cc.slh.task.FetchSLItemsTask.IFetchSLItemsTaskCaller;
import edu.cmu.cc.slh.view.adapter.ActiveSLViewListAdapter;
import edu.cmu.cc.slh.view.adapter.ActiveSLViewListAdapter.IDeleteSLItemCaller;
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

/**
 *  DESCRIPTION: Active shopping list activity
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 5, 2013
 */
@SuppressLint("UseSparseArrays")
public class ActiveSLActivity extends AbstractAsyncListActivity
implements IFetchSLItemsTaskCaller, IDeleteSLItemCaller, ISLItemDialogCaller,
IOptionsMenuHandler {

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
		
		fetchActiveShoppingListItems();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		asyncTaskHandler = new Handler();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return prepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return handleOptionsMenuItemSelection(item);
	}


	@Override
	public boolean prepareOptionsMenu(Menu menu) {
		
		if (ApplicationState.getInstance().getCurrentSL() == null) {
			return false;
		}
		
		menuItems = new HashMap<Integer, MenuItem>(3);
		
		menuItems.put(R.string.sl_item_add, 
				menu.add(R.string.sl_item_add).setIcon(R.drawable.add));
		menuItems.put(R.string.settings_triangulation_onOff, 
				menu.add(R.string.settings_triangulation_onOff).setIcon(R.drawable.add));
		menuItems.put(R.string.sl_show_summary, 
				menu.add(R.string.sl_show_summary).setIcon(R.drawable.add));
		
		setMenuItemState(R.string.sl_item_add, true, true);
		setMenuItemState(R.string.settings_triangulation_onOff, true, true);
		setMenuItemState(R.string.sl_show_summary, true, true);
		
		return true;
	}

	@Override
	public boolean handleOptionsMenuItemSelection(final MenuItem item) {
		
		if (ApplicationState.getInstance().getCurrentSL() == null) {
			return false;
		}
		
		Runnable callback = new Runnable() {
			
			@Override
			public void run() {
				if (item.getTitle().equals(
						getString(R.string.sl_item_add))) {
					
					ShoppingListItem newSLItem = new ShoppingListItem();
					newSLItem.setShoppingList(
							ApplicationState.getInstance().getCurrentSL());
					
					showSLItemDialog(newSLItem);
				} else if (item.getTitle().equals(
						getString(R.string.settings_triangulation_onOff))) {
					//TODO:
				} else if (item.getTitle().equals(
						getString(R.string.sl_show_summary))) {
					//TODO:
				}
			}
		};
		
		Message osMessage = Message.obtain(this.asyncTaskHandler, callback);
		osMessage.sendToTarget();
		
		return true;
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
								ActiveSLActivity.this.finish();
							}
						};
				
				Logger.logErrorAndAlert(ActiveSLActivity.this, 
						ActiveSLActivity.class, errorMsg, t, dialogListener);
			}
		};
		
		Message osMessage = Message.obtain(this.asyncTaskHandler, callback);
		osMessage.sendToTarget();
	}
	
	@Override
	public void onFetchSLItemsTaskSucceeded(List<ShoppingListItem> items) {
		
		ShoppingList currentSL = ApplicationState.getInstance().getCurrentSL();
		if (currentSL != null) {
			currentSL.setItems(items);
		}
		
		refreshGUI();
	}
	
	@Override
	public void onSLItemDeleted() {
		fetchActiveShoppingListItems();
	}
	
	@Override
	public void onSLItemUpdated() {
		
		ShoppingListItem item = 
				ApplicationState.getInstance().getCurrentSLItem();
		
		saveSLItem(item);
		
		Runnable callback = new Runnable() {
			
			@Override
			public void run() {
				fetchActiveShoppingListItems();
			}
		};
		
		Message osMessage = Message.obtain(this.asyncTaskHandler, callback);
		osMessage.sendToTarget();
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void saveSLItem(final ShoppingListItem item) {
		
		try {
			new SLItemDAO().save(item);
		} catch (Exception e) {
			final String errMsg = StringUtils.getLimitedString(
					getString(R.string.sl_item_save_failed, 
							e.getMessage()), 200, "...");
			
			Logger.logErrorAndAlert(this, getClass(), errMsg, e);
		}
	}
	
	private void setMenuItemState(int menuItemTitleResID, 
			boolean visible, boolean enabled) {
		
		MenuItem menuItem = menuItems.get(menuItemTitleResID);
		menuItem.setVisible(visible);
		menuItem.setEnabled(enabled);
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM 
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}
	
	private void fetchActiveShoppingListItems() {
		
		ShoppingList currentSL = ApplicationState.getInstance().getCurrentSL();
		
		if (currentSL != null) {
			new FetchSLItemsTask(this).execute(new ShoppingList[]{currentSL});
		}
	}
	
	private void setListAdapter() {
		
		Collection<ItemCategory> categories = 
				ApplicationState.getInstance().getCategories().values();
		
		for (ItemCategory category : categories) {
			List<ShoppingListItem> categoryItems = getSLItemsByCategory(category);
			category.setItems(categoryItems);
		}
		
		ActiveSLViewListAdapter adapter = 
				new ActiveSLViewListAdapter(this, categories, this);
		
		setListAdapter(adapter);
	}
	
	private List<ShoppingListItem> getSLItemsByCategory(ItemCategory category) {
		
		List<ShoppingListItem> categoryItems = new ArrayList<ShoppingListItem>();
		
		ShoppingList currentSL = ApplicationState.getInstance().getCurrentSL();
		
		for (ShoppingListItem item : currentSL.getItems()) {
			if (category.equals(item.getCategory())) {
				categoryItems.add(item);
			}
		}
		
		return categoryItems;
	}
	
	private String getAsyncTaskFailedMessage(Class<?> taskClass, Throwable t) {
		
		int msgResID = R.string.error_unspecified;
		
		if (taskClass == FetchSLItemsTask.class) {
			msgResID = R.string.sl_item_error_fetch;
		} else {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException("Unexpected class: " 
							+ taskClass.toString()));
		}
		
		return StringUtils.getLimitedString(
				getString(msgResID, t.getMessage()), 200, "...");
	}
	
	private void showSLItemDialog(final ShoppingListItem item) {
		
		ApplicationState.getInstance().setCurrentSLItem(item);
		
		DialogFragment slItemDialog = SLItemDialog.newInstance(this);
		slItemDialog.show(getFragmentManager(), null);
	}
	
	private void prepareListClick() {
		
		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				
				ShoppingListItem item = 
						(ShoppingListItem) getListAdapter().getItem(position);
				
				showSLItemDialog(item);
			}
		});
	}
	
	private void refreshGUI() {
		setListAdapter();
		onContentChanged();
		prepareListClick();
	}
	
}
