/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.os.AsyncTask;

import edu.cmu.cc.android.activity.async.IAsyncActivity;
import edu.cmu.cc.android.service.soap.SoapWebService;
import edu.cmu.cc.android.service.soap.util.SoapUtils;
import edu.cmu.cc.android.util.DeviceUtils;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActivationAdapter;
import edu.cmu.cc.slh.adapter.ItemCategoryAdapter;
import edu.cmu.cc.slh.adapter.SLAdapter;
import edu.cmu.cc.slh.dao.ItemCategoryDAO;
import edu.cmu.cc.slh.dao.SLDAO;
import edu.cmu.cc.slh.dao.SLItemDAO;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;

/**
 *  This task is used for downloading from server or fetching
 *  from the local DB ItemCategories, ShoppingLists with 
 *  ShoppingList items.
 *	
 *  @author Azamat Samiyev
 *	@version 2.0
 *  Date: Jul 31, 2013
 */
public class FetchSLsTask extends AsyncTask<Void, Void, List<ShoppingList>> {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private IFetchSLTaskCaller caller;
	
	private ItemCategoryDAO categoryDAO;
	
	private SLDAO slDAO;
	
	private SLItemDAO slItemDAO;
	
	private String memberId;
	
	private boolean errorState;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public FetchSLsTask(Context ctx, IFetchSLTaskCaller caller) {
		super();
		
		this.ctx = ctx;
		this.caller = caller;
	}

	//-------------------------------------------------------------------------
	// AsyncTask METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		//---------------------------------------------------
		// Checking whether the network is connected or not.
		// If not, we cancel the execution of the task.
		//---------------------------------------------------
		if (!DeviceUtils.isNetworkConnectedElseAlert(
				ctx, getClass(), R.string.ws_error_noconnection)) {
			this.cancel(true);
		}
		
		caller.showProgressDialog(R.string.sl_all_loading);
		
		categoryDAO = new ItemCategoryDAO();
		slDAO = new SLDAO();
		slItemDAO = new SLItemDAO();
		
		errorState = false;
	}
	
	@Override
	protected List<ShoppingList> doInBackground(Void... params) {
		
		try {
			
			//---------------------------------------------------
			// Getting User Membership ID
			//---------------------------------------------------
			memberId = ActivationAdapter.retrieveMemberId();
			if (StringUtils.isNullOrEmpty(memberId)) {
				throw new IllegalArgumentException("MemberID is null " +
						"or empty!");
			}
			
			//---------------------------------------------------
			// Here we start another task which tries to download
			// warehouses from the server in a parallel thread.
			//---------------------------------------------------
//			fetchWarehouses();
			
			//---------------------------------------------------
			// Item Categories Version checking
			//---------------------------------------------------
			int localCategoriesVersion = retrieveLocalCategoryVersion();
			int serverCategoriesVersion = retrieveServerCategoryVersion();
			
			if (localCategoriesVersion != serverCategoriesVersion) {
				deleteLocalCategories();
				retrieveAndSaveServerCategories();
				saveLocalCategoriesVersion(serverCategoriesVersion);
			}
			
			loadLocalCategories();
			
			//---------------------------------------------------
			// ShoppingLists Version checking
			//---------------------------------------------------
			int localMemberVersion = retrieveLocalMemberVersion();
			int serverMemberVersion = retrieveServerMemberVersion();
			
			if (localMemberVersion != serverMemberVersion) {
				deleteLocalSLs();
				retrieveAndSaveServerSLs();
				saveLocalMemberVersion(serverMemberVersion);
			}
			
			return retrieveLocalSLs();
			
		} catch (Throwable t) {
			errorState = true;
			caller.onAsyncTaskFailed(this.getClass(), t);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(List<ShoppingList> list) {
		super.onPostExecute(list);
		
		slItemDAO.close();
		slDAO.close();
		categoryDAO.close();
		
		caller.dismissProgressDialog();
		if (!errorState) {
			caller.onFetchSLTaskSucceeded(list);
		}
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Starting Warehouses fetching task
	 */
	private void fetchWarehouses() {
		new FetchWarehousesTask(ctx).execute();
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS: ItemCategories
	//-------------------------------------------------------------------------
	
	/**
	 * Gets local ItemCategories version number
	 * 
	 * @return item categories local version number
	 */
	private int retrieveLocalCategoryVersion() {
		return ItemCategoryAdapter.retrieveVersion();
	}
	
	/**
	 * Calls Web service to get the server ItemCategories version number
	 * 
	 * @return server categories version number
	 * @throws Throwable - web service call exceptions
	 */
	private int retrieveServerCategoryVersion() throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving Categories Version number from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_category_namespace), 
				ctx.getString(R.string.ws_category_url));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_category_method_retrieveVersion), 
				SecureWSHelper.initWSArguments(ctx, memberId));
		
		return parseCategoriesVersion(response);
	}
	
	/**
	 * Parses categories version number from the SoapObject
	 * 
	 * @param root - SoapObject
	 * @return server categories version number
	 */
	private int parseCategoriesVersion(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Deletes all the local ItemCategory objects from the local DB
	 */
	private void deleteLocalCategories() {
		categoryDAO.deleteAll();
	}
	
	/**
	 * Calls the web service to get the list of ItemCategories and
	 * saves them in the local DB.
	 * 
	 * @throws Throwable - web service call exceptions
	 */
	private void retrieveAndSaveServerCategories() throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving ItemCategories from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_category_namespace), 
				ctx.getString(R.string.ws_category_url));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_category_method_retrieveCategories), 
				SecureWSHelper.initWSArguments(ctx, memberId));
		
		parseAndSaveCategoriesXML(response);
	}
	
	/**
	 * Parses Soap response and creates ItemCategory objects based on that. 
	 * After, it saves them in the local DB.
	 * 
	 * @param root - SoapObject
	 */
	private void parseAndSaveCategoriesXML(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		for (int i = 0; i < result.getPropertyCount(); i++) {
			
			SoapObject categoryProperty = 
					(SoapObject) result.getProperty(i);
			
			ItemCategory category = new ItemCategory();
			category.setId(SoapUtils.getLongPropertyValue(categoryProperty, 
					ctx.getString(R.string.ws_category_property_id)));
			category.setName(categoryProperty.getPropertyAsString(
					ctx.getString(R.string.ws_category_property_name)));
			
			saveCategory(category);
		}
		
	}
	
	/**
	 * Saves the given ItemCategory object in the local DB
	 * 
	 * @param category - ItemCategory object to be saved
	 */
	private void saveCategory(ItemCategory category) {
		categoryDAO.save(category);
	}
	
	/**
	 * Saves the updated categories version as the local categories version
	 * 
	 * @param version - categories version
	 */
	private void saveLocalCategoriesVersion(int version) {
		ItemCategoryAdapter.persistVersion(version);
	}
	
	/**
	 * Loads all categories from the local DB and stores them
	 * in the central application state
	 */
	private void loadLocalCategories() {
		
		List<ItemCategory> categories = categoryDAO.getAll();
		
		Map<Long, ItemCategory> categoriesMap = 
				new HashMap<Long, ItemCategory>(categories.size());
		
		for (ItemCategory category : categories) {
			categoriesMap.put(category.getId(), category);
		}
		
		ApplicationState.getInstance().setCategories(categoriesMap);
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS: ShoppingLists
	//-------------------------------------------------------------------------
	
	/**
	 * Gets local member version number
	 * 
	 * @return local member version
	 */
	private int retrieveLocalMemberVersion() {
		return SLAdapter.retrieveMemberVersion();
	}
	
	/**
	 * Gets the current version of the member table from the server.
	 * 
	 * @return server member version
	 * @throws Throwable - web service call exceptions
	 */
	private int retrieveServerMemberVersion() throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving MemberVersion number from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_member_namespace),
				ctx.getString(R.string.ws_member_url));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_member_method_retrieveVersion), 
				SecureWSHelper.initWSArguments(ctx, memberId));
		
		return parseMemberVersion(response);
	}
	
	/**
	 * Parses member version number from the SoapObject
	 * 
	 * @param root - SoapObject
	 * @return server member version number
	 */
	private int parseMemberVersion(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		return SoapUtils.getIntPropertyValue(result, 
				ctx.getString(R.string.ws_property_version));
	}
	
	/**
	 * Deletes all ShoppingLists and ShoppingList items from the local DB
	 */
	private void deleteLocalSLs() {
		slItemDAO.deleteAll();
		slDAO.deleteAll();
	}
	
	/**
	 * Calls web service to get the list of ShoppingLists 
	 * with ShoppingListItems and saves them in local DB.
	 * 
	 * @throws Throwable - web service call exceptions
	 */
	private void retrieveAndSaveServerSLs() throws Throwable {
		
		Logger.logDebug(this.getClass(), 
				"Retrieving ShoppingLists from the server...");
		
		SoapWebService service = new SoapWebService(
				ctx.getString(R.string.ws_sl_namespace),
				ctx.getString(R.string.ws_sl_url));
		
		SoapObject response = service.invokeMethod(
				ctx.getString(R.string.ws_sl_method_retrieveSLs), 
				SecureWSHelper.initWSArguments(ctx, memberId));
		
		parseAndSaveSLsXML(response);
	}
	
	/**
	 * Parses Soap response and creates ShoppingList objects form it.
	 * After, saves them in the local DB. 
	 * 
	 * @param root - SoapObject
	 */
	private void parseAndSaveSLsXML(SoapObject root) {
		
		SoapObject result = (SoapObject) root.getProperty(0);
		
		SoapUtils.checkForException(ctx, result);
		
		for (int i = 0; i < result.getPropertyCount(); i++) {
			SoapObject slProperty = 
					(SoapObject) result.getProperty(i);
			
			ShoppingList sl = new ShoppingList();
			sl.setId(SoapUtils.getLongPropertyValue(slProperty, 
					ctx.getString(R.string.ws_sl_property_id)));
			sl.setName(slProperty.getPropertyAsString(
					ctx.getString(R.string.ws_sl_property_name)));
			sl.setDate(new Date(
					SoapUtils.getLongPropertyValue(slProperty, 
							ctx.getString(R.string.ws_sl_property_date))));
			sl.setDescription(slProperty.getPropertyAsString(
					ctx.getString(R.string.ws_sl_property_desc)));
			sl.setVersion(SoapUtils.getIntPropertyValue(slProperty, 
					ctx.getString(R.string.ws_property_version)));
			
			parseSLItemsXML(slProperty, sl);
			
			saveSL(sl);
		}
		
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS: ShoppingListItems
	//-------------------------------------------------------------------------
	
	/**
	 * Parses and creates ShoppingListItem objects and ads them to the 
	 * corresponding ShoppingList object.
	 * 
	 * @param slProperty - ShoppingList property
	 * @param sl - ShoppingList object
	 */
	private void parseSLItemsXML(SoapObject slProperty, ShoppingList sl) {
		
		for (int i = 0; i < slProperty.getPropertyCount(); i++) {
			
			if (SoapUtils.isComplexProperty(slProperty.getProperty(i))) {
				SoapObject itemProperty = (SoapObject) slProperty.getProperty(i);
				
				ShoppingListItem item = new ShoppingListItem();
				
				item.setId(SoapUtils.getLongPropertyValue(itemProperty, 
						ctx.getString(R.string.ws_sl_item_property_id)));
				item.setShoppingList(sl);
				item.setCategory(getCategoryById(
						SoapUtils.getLongPropertyValue(itemProperty, 
								ctx.getString(R.string.ws_sl_item_property_category))));
				item.setName(itemProperty.getPropertyAsString(
						ctx.getString(R.string.ws_sl_item_property_name)));
				item.setQuantity(SoapUtils.getIntPropertyValue(itemProperty, 
						ctx.getString(R.string.ws_sl_item_property_quantity)));
				item.setPrice(new BigDecimal(SoapUtils.getDoublePropertyValue(
						itemProperty, 
						ctx.getString(R.string.ws_sl_item_property_price))));
				item.setUnit(SoapUtils.getIntPropertyValue(itemProperty, 
						ctx.getString(R.string.ws_sl_item_property_unit)));
				item.setDescription(itemProperty.getPropertyAsString(
						ctx.getString(R.string.ws_sl_item_property_desc)));
				
				sl.addItem(item);
			}
		}
		
	}
	
	/**
	 * Finds the ItemCategory object by the given category id
	 * 
	 * @param id - category id
	 * @return ItemCategory object
	 */
	private ItemCategory getCategoryById(long id) {
		return ApplicationState.getInstance().getCategories().get(id);
	}
	
	/**
	 * Saves the given ShoppingList object and its ShoppingListItems
	 * in the local DB.
	 * 
	 * @param sl - ShoppingList object
	 */
	private void saveSL(ShoppingList sl) {
		slDAO.save(sl);
		for (ShoppingListItem item : sl.getItems()) {
			slItemDAO.save(item);
		}
	}
	
	/**
	 * Updates the local Member version number
	 * 
	 * @param version - new version number
	 */
	private void saveLocalMemberVersion(int version) {
		SLAdapter.persistMemberVersion(version);
	}
	
	/**
	 * Gets all the ShoppingList objects belonging to the current member 
	 * from the local DB.
	 * 
	 * @return ShoppingList objects
	 */
	private List<ShoppingList> retrieveLocalSLs() {
		
		return slDAO.getAll();
	}
	
	//-------------------------------------------------------------------------
	// INNER INTERFACE
	//-------------------------------------------------------------------------
	
	/**
	 * All the callers of this task should implement this interface in order
	 * to get informed of the resul of the task.
	 */
	public interface IFetchSLTaskCaller extends IAsyncActivity {
		
		/**
		 * Once the fetching task completes successfully, task class will
		 * provide, newly fetched or loaded from the local DB, the list of
		 * ShoppingList objects 
		 * 
		 * @param list - ShoppingList objects
		 */
		public void onFetchSLTaskSucceeded(List<ShoppingList> list);
		
	}

}
