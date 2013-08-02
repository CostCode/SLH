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
import edu.cmu.cc.slh.activity.listener.ISLItemStateListener;
import edu.cmu.cc.slh.dialog.SLItemDialog;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;
import edu.cmu.cc.slh.task.DeleteSLItemTask;
import edu.cmu.cc.slh.task.FetchSLItemsTask;
import edu.cmu.cc.slh.task.FetchSLItemsTask.IFetchSLItemsTaskCaller;
import edu.cmu.cc.slh.task.SaveSLItemTask;
import edu.cmu.cc.slh.view.adapter.ActiveSLViewListAdapter;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  DESCRIPTION: Active shopping list activity
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 5, 2013
 */
@SuppressLint("UseSparseArrays")
public class SLItemsActivity extends AbstractAsyncListActivity
implements IFetchSLItemsTaskCaller, ISLItemStateListener, ITabActivity {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Map<Integer, MenuItem> menuItems;

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	@Override
	public ActiveSLViewListAdapter getListAdapter() {
		return (ActiveSLViewListAdapter) super.getListAdapter();
	}
	

	//-------------------------------------------------------------------------
	// ACTIVITY METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_sl);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setActivityTitle();
		fetchActiveShoppingListItems();
	}
	
	//-------------------------------------------------------------------------
	// ITabActivity METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void init(ITabHostActivity tabHost) {}
	
	@Override
	public boolean prepareOptionsMenu(Menu menu) {
		
		menu.clear();
		
		if (ApplicationState.getInstance().getCurrentSL() == null) {
			return false;
		}
		
		menuItems = new HashMap<Integer, MenuItem>(1);
		
		menuItems.put(R.string.sl_item_add, 
				menu.add(R.string.sl_item_add).setIcon(R.drawable.add));
		
		setMenuItemState(R.string.sl_item_add, true, true);
		
		return true;
	}

	@Override
	public boolean handleOptionsMenuItemSelection(final MenuItem item) {
		
		if (ApplicationState.getInstance().getCurrentSL() == null) {
			return false;
		}
		
		addTaskToUIQueue(new Runnable() {
			
			@Override
			public void run() {
				if (item.getTitle().equals(
						getString(R.string.sl_item_add))) {
					
					ShoppingListItem newSLItem = new ShoppingListItem();
					newSLItem.setShoppingList(
							ApplicationState.getInstance().getCurrentSL());
					
					showSLItemDialog(newSLItem);
				}
			}
		});
		
		return true;
	}
	
	@Override
	public void refresh() {
		
		addTaskToUIQueue(new Runnable() {
			
			@Override
			public void run() {
				refreshGUI();
			}
		});
	}


	@Override
	public void onAsyncTaskSucceeded(final Class<?> taskClass) {
		
		addTaskToUIQueue(new Runnable() {
			
			@Override
			public void run() {
				
				if (taskClass == SaveSLItemTask.class) {
					Toast.makeText(SLItemsActivity.this,
							R.string.sl_item_save_success, 
							Toast.LENGTH_LONG).show();
				} else if (taskClass == DeleteSLItemTask.class) {
					Toast.makeText(SLItemsActivity.this,
							R.string.sl_item_delete_success, 
							Toast.LENGTH_LONG).show();
				}
				
				fetchActiveShoppingListItems();
			}
		});
	}
	
	@Override
	public void onAsyncTaskFailed(Class<?> taskClass, final Throwable t) {
		
		final String errorMsg = getAsyncTaskFailedMessage(taskClass, t);
		
		addTaskToUIQueue(new Runnable() {
			
			@Override
			public void run() {
				Logger.logErrorAndAlert(SLItemsActivity.this, 
						SLItemsActivity.class, errorMsg, t);
			}
		});
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
	public void onSLItemUpdated() {
		
		ShoppingListItem item = 
				ApplicationState.getInstance().getCurrentSLItem();
		
		new SaveSLItemTask(SLItemsActivity.this, SLItemsActivity.this)
			.execute(new ShoppingListItem[]{item});
	}
	
	@Override
	public void onSLItemDeleted() {
		
		ShoppingListItem item = 
				ApplicationState.getInstance().getCurrentSLItem();
		
		new DeleteSLItemTask(SLItemsActivity.this, SLItemsActivity.this)
			.execute(new ShoppingListItem[]{item});
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
		
		if (currentSL != null) {
			for (ShoppingListItem item : currentSL.getItems()) {
				if (category.equals(item.getCategory())) {
					categoryItems.add(item);
				}
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
	
	private void setActivityTitle() {
		
		TextView tvTitle = (TextView) findViewById(R.id.tv_active_sl_title);
		
		ShoppingList currentSL = ApplicationState.getInstance().getCurrentSL();
		
		if (currentSL != null) {
			tvTitle.setText(currentSL.getName());
		} else {
			tvTitle.setText("");
		}
	}
	
	private void refreshGUI() {
		setActivityTitle();
		setListAdapter();
		onContentChanged();
		prepareListClick();
	}
	
}
