/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.os.AsyncTask;

import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ShoppingListAdapter;
import edu.cmu.cc.slh.model.ShoppingList;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
public class FetchShoppingListsTask 
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
	
	public FetchShoppingListsTask(Context ctx, 
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
	protected List<ShoppingList> doInBackground(Void... params) {
		
		errorState = false;
		
		try {
			
			int localVersion = getLocalVersion();
			int serverVersion = getServerVersion();
			
			if (localVersion >= serverVersion) {
				return retrieveFromLocal();
			} else {
				return retrieveFromServer();
			}
			
		} catch (Exception e) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), e);
		}
		
		return null;
	}


	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private int getLocalVersion() {
		return ShoppingListAdapter.retrieveVersion();
	}
	
	private int getServerVersion() throws Exception {
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_url), 
				ctx.getString(R.string.ws_namespace));
		
		SoapObject soapResponse = service.invokeMethod(
				ctx.getString(R.string.ws_shoppinglists_retrieveVersion), 
				null);
		
		String strResult = soapResponse.getProperty(
				ctx.getString(R.string.ws_shoppinglists_version)).toString();
		
		int version = Integer.parseInt(strResult);
		
		Logger.logDebug(this.getClass(), 
				String.format("Shopping Lists version[server]: %d", version));
		
		return version;
	}
	
	private List<ShoppingList> retrieveFromLocal() {
		return null;
	}
	
	private List<ShoppingList> retrieveFromServer() {
		return null;
	}
	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------
	
	public interface IFetchShoppingListsTaskCaller extends IAsyncActivity {
		
		public void onFetchShoppingListsTaskSucceeded(List<ShoppingList> list);
		
	}

}
