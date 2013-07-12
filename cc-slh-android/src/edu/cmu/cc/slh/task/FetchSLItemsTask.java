/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;

import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.dao.ItemCategoryDAO;
import edu.cmu.cc.slh.dao.SLItemDAO;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
public class FetchSLItemsTask 
extends AsyncTask<ShoppingList, Void, List<ShoppingListItem>>{

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private IFetchSLItemsTaskCaller caller;
	
	private boolean errorState;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public FetchSLItemsTask(IFetchSLItemsTaskCaller caller) {
		
		this.caller = caller;
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PROTECTED METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		caller.showProgressDialog(
				R.string.sl_item_loading);
	}
	
	@Override
	protected List<ShoppingListItem> doInBackground(ShoppingList... slList) {
		
		errorState = false;
		
		try {
			
			fetchItemCategories();
			
			ShoppingList sl = slList[0];
			
			return retrieveFromLocal(sl);
			
		} catch (Exception e) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), e);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(List<ShoppingListItem> items) {
		super.onPostExecute(items);
		
		caller.dismissProgressDialog();
		if (!errorState) {
			caller.onFetchSLItemsTaskSucceeded(items);
		}
	}


	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void fetchItemCategories() {
		List<ItemCategory> categories = new ItemCategoryDAO().getAll();
		
		if (categories == null || categories.size() == 0) {
			addSampleItemCategories();
			categories = new ItemCategoryDAO().getAll();
		}
		
		Map<Long, ItemCategory> categoriesMap = 
				new HashMap<Long, ItemCategory>(categories.size());
		
		for (ItemCategory category : categories) {
			categoriesMap.put(category.getId(), category);
		}
		
		ApplicationState.getInstance().setCategories(categoriesMap);
	}
	
	private List<ShoppingListItem> retrieveFromLocal(final ShoppingList sl) {
		
		return new SLItemDAO().getAll(sl);
	}
	
	private void addSampleItemCategories() {
		
		ItemCategoryDAO categoryDAO = new ItemCategoryDAO();
		
		// Sample Category #1
		ItemCategory category1 = new ItemCategory();
		category1.setName("Electronics");
		category1.setDescription("Electronic appliances");
		categoryDAO.save(category1);
		
		// Sample Category #2
		ItemCategory category2 = new ItemCategory();
		category2.setName("Furniture");
		category2.setDescription("House furniture");
		categoryDAO.save(category2);
		
		// Sample Category #3
		ItemCategory category3 = new ItemCategory();
		category3.setName("Food and Beverages");
		category3.setDescription("All food related stuff");
		categoryDAO.save(category3);
	}
	
	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------
	
	public interface IFetchSLItemsTaskCaller extends IAsyncActivity {
		
		public void onFetchSLItemsTaskSucceeded(List<ShoppingListItem> items);
		
	}

}
