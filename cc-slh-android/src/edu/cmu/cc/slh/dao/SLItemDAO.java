/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.ApplicationState;
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
@SuppressLint("DefaultLocale")
public class SLItemDAO {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** TABLE NAME */
	static final String TABLE_NAME = "shoppinglist_item";
	
	/** PRIMARY KEY: Shopping list item id */
	static final String COLUMN_ID = "id";
	
	/** COLUMN: Parent shopping list id */
	static final String COLUMN_SHOPPINGLIST = "shoppinglistId";
	
	/** COLUMN: Shopping list item category id */
	static final String COLUMN_CATEGORY = "categoryId";
	
	/** COLUMN: Shopping list item name */
	static final String COLUMN_NAME = "name";
	
	/** COLUMN: Shopping list item quantity */
	static final String COLUMN_QUANTITY = "quantity";
	
	/** COLUMN: Shopping list item price */
	static final String COLUMN_PRICE = "price";
	
	/** COLUMN: Shopping list item unit */
	static final String COLUMN_UNIT = "unit";
	
	/** COLUMN: Shopping list item description */
	static final String COLUMN_DESC = "description";
	
	
	/** Create ShoppingListItem table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_SHOPPINGLIST + " INTEGER, " +
			COLUMN_CATEGORY + " INTEGER, " +
			COLUMN_NAME + " TEXT, " +
			COLUMN_QUANTITY + " INTEGER, " +
			COLUMN_PRICE + " REAL, " +
			COLUMN_UNIT + " INTEGER, " +
			COLUMN_DESC + " TEXT)";
	
	static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public SLItemDAO() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Retrieves all the items of the given shopping list
	 * @param shoppingListID - id of the shopping list
	 * @return list of items
	 */
	public List<ShoppingListItem> getAll(final ShoppingList parent) {
		
		if (parent == null) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException("Parent shopping list is null"));
		}
		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<ShoppingListItem> list = null;
		
		try {
			db = new DBHelper().getWritableDatabase();
			
			final String sqlWhere = String.format("%s=%d", 
					COLUMN_SHOPPINGLIST, parent.getId());
			
			cursor = db.query(TABLE_NAME, null, sqlWhere, 
					null, null, null, COLUMN_NAME);
			
			int rowCount = cursor.getCount();
			Logger.logDebug(getClass(), String
					.format("Retrieved %d shopping list items...", rowCount));
			
			list = new ArrayList<ShoppingListItem>(rowCount);
			
			while(cursor.moveToNext()) {
				ShoppingListItem slItem = new ShoppingListItem();
				
				slItem.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
				
				slItem.setShoppingList(parent);
				
				slItem.setName(
						cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
				
				slItem.setQuantity(
						cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)));
				
				slItem.setPrice(new BigDecimal(
						cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))));
				
				slItem.setUnit(
						cursor.getInt(cursor.getColumnIndex(COLUMN_UNIT)));
				
				slItem.setDescription(
						cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
				
				long categoryId = cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORY));
				ItemCategory category = ApplicationState.getInstance()
						.getCategories().get(categoryId);
				slItem.setCategory(category);
				
				list.add(slItem);
			}
		} catch (Throwable t) {
			Logger.logErrorAndThrow(getClass(), t);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		
		return list;
	}
	
	/**
	 * Saves the given Shopping list item into the local DB
	 * @param slItem - shopping list item to be saved
	 * @return saved shopping list item with attached id number
	 */
	public ShoppingListItem save(ShoppingListItem slItem) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save ShoppingListItem [%s]", slItem));
		
		if (slItem == null) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException("Saving null " +
							"ShoppingListItem is not allowed"));
		}
		
		SQLiteDatabase db = null;
		
		try {
			db = new DBHelper().getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(COLUMN_SHOPPINGLIST, slItem.getShoppingList().getId());
			values.put(COLUMN_CATEGORY, slItem.getCategory().getId());
			values.put(COLUMN_NAME, slItem.getName());
			values.put(COLUMN_QUANTITY, slItem.getQuantity());
			values.put(COLUMN_PRICE, slItem.getPrice().doubleValue());
			values.put(COLUMN_UNIT, slItem.getUnit());
			values.put(COLUMN_DESC, slItem.getDescription());
			
			if (slItem.getId() > 0) {
				db.update(TABLE_NAME, values, 
						String.format("%s=%d", COLUMN_ID, slItem.getId()), null);
			} else {
				long id = db.insert(TABLE_NAME, null, values);
				slItem.setId(id);
			}
			
			Logger.logDebug(getClass(), String.format("ShoppingListItem " +
					"was saved in the local DB. [%s]", slItem));
			
		} catch (Throwable t) {
			Logger.logErrorAndThrow(getClass(), t);
		} finally {
			if (db != null) {
				db.close();
			}
		}
		
		return slItem;
	}
	
	/**
	 * Removes the given Shopping list item from the local DB
	 * @param slItem - shopping list item to be removed
	 */
	public void delete(ShoppingListItem slItem) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to delete ShoppingListItem [%s]", slItem));
		
		SQLiteDatabase db = null;
		try {
			db = new DBHelper().getWritableDatabase();
			
			int deleted = db.delete(TABLE_NAME, 
					String.format("%s=%d", COLUMN_ID, slItem.getId()), null);
			
			Logger.logDebug(getClass(), String.format("[%d] " +
					"ShoppingListItem records were deleted from the DB", deleted));
		} catch (Throwable t) {
			Logger.logErrorAndThrow(getClass(), t);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	/**
	 * Removes all the shopping list items from the local DB
	 */
	public void deleteAll() {
		SQLiteDatabase db = new DBHelper().getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}
