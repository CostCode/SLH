/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.List;

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
		ApplicationState.getInstance().setCategories(categories);
	}
	
	private List<ShoppingListItem> retrieveFromLocal(ShoppingList sl) {
		
		return new SLItemDAO().getAll(sl.getId());
	}
	
	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------
	
	public interface IFetchSLItemsTaskCaller extends IAsyncActivity {
		
		public void onFetchSLItemsTaskSucceeded(List<ShoppingListItem> items);
		
	}

}
