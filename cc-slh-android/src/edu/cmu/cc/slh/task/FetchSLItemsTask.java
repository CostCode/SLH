/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.List;

import android.os.AsyncTask;

import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.dao.SLItemDAO;
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
	
	private SLItemDAO itemDAO;
	
	private boolean errorState;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public FetchSLItemsTask(IFetchSLItemsTaskCaller caller) {
		super();
		
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
		
		itemDAO = new SLItemDAO();
		
		errorState = false;
	}
	
	@Override
	protected List<ShoppingListItem> doInBackground(ShoppingList... params) {
		
		try {
			
			if (params == null || params[0] == null) {
				throw new RuntimeException("Invalid input parameter: " +
						"ShoppingList is null");
			}
			
			ShoppingList sl = params[0];
			
			return retrieveFromLocal(sl);
			
		} catch (Throwable t) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), t);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(List<ShoppingListItem> items) {
		super.onPostExecute(items);
		
		itemDAO.close();
		
		caller.dismissProgressDialog();
		if (!errorState) {
			caller.onFetchSLItemsTaskSucceeded(items);
		}
	}


	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private List<ShoppingListItem> retrieveFromLocal(final ShoppingList sl) {
		
		return itemDAO.getAll(sl);
	}
	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------
	
	public interface IFetchSLItemsTaskCaller extends IAsyncActivity {
		
		public void onFetchSLItemsTaskSucceeded(List<ShoppingListItem> items);
		
	}

}
