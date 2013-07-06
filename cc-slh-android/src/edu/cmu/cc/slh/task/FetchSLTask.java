/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.dao.SLDAO;
import edu.cmu.cc.slh.model.ShoppingList;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
public class FetchSLTask 
extends AsyncTask<Void, Void, List<ShoppingList>>{

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private IFetchShoppingListsTaskCaller caller;
	
	private boolean errorState;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public FetchSLTask(Context ctx, 
			IFetchShoppingListsTaskCaller caller) {
		
		this.ctx = ctx;
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
				R.string.shoppinglist_all_progressDialogText);
	}
	
	@Override
	protected List<ShoppingList> doInBackground(Void... params) {
		
		errorState = false;
		
		try {
			
			return retrieveFromLocal();
			
		} catch (Exception e) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), e);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(List<ShoppingList> list) {
		super.onPostExecute(list);
		
		caller.dismissProgressDialog();
		if (!errorState) {
			caller.onFetchShoppingListsTaskSucceeded(list);
		}
	}


	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private List<ShoppingList> retrieveFromLocal() {
		
		return new SLDAO().getAll();
	}
	
	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------
	
	public interface IFetchShoppingListsTaskCaller extends IAsyncActivity {
		
		public void onFetchShoppingListsTaskSucceeded(List<ShoppingList> list);
		
	}

}
