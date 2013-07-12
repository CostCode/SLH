/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.model.ShoppingList;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
public class SLDAO {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** TABLE NAME */
	static final String TABLE_NAME = "shoppinglist";
	
	/** PRIMARY KEY: Shopping list id */
	static final String COLUMN_ID = "id";
	
	/** COLUMN: Shopping list name */
	static final String COLUMN_NAME = "name";
	
	/** COLUMN: Shopping list creation date */
	static final String COLUMN_DATE = "date";
	
	/** COLUMN: Item category description */
	static final String COLUMN_DESC = "description";
	
	
	/** Create ShoppingList table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT UNIQUE, " +
			COLUMN_DATE + " INTEGER, " +
			COLUMN_DESC + " TEXT)";
	
	static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public SLDAO() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Retrieves all the shopping list items from the database
	 * @return - shopping list items
	 */
	public List<ShoppingList> getAll() {
		
		SQLiteDatabase db = new DBHelper().getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, 
				COLUMN_NAME);
		
		int rowCount = cursor.getCount();
		Logger.logDebug(getClass(), 
				String.format("Retrieved %d shopping lists...", rowCount));
		
		List<ShoppingList> list = new ArrayList<ShoppingList>(rowCount);
		while(cursor.moveToNext()) {
			ShoppingList sl = new ShoppingList();
			
			sl.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
			sl.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
			sl.setDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
			sl.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
			
			list.add(sl);
		}
		
		cursor.close();
		db.close();
		
		return list;
	}
	
	/**
	 * Saves given shopping list object into the database.
	 * @param sl - shopping list object to be saved
	 * @return saved shopping list object with attached id number
	 */
	public ShoppingList save(ShoppingList sl) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save ShoppingList [%s]", sl));
		
		SQLiteDatabase db = new DBHelper().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, sl.getName());
		values.put(COLUMN_DATE, sl.getDate().getTime());
		values.put(COLUMN_DESC, sl.getDescription());
		
		if (sl.getId() > 0) {
			db.update(TABLE_NAME, values, COLUMN_ID + "=" + sl.getId(), null);
		} else {
			long id = db.insert(TABLE_NAME, null, values);
			sl.setId(id);
		}
		
		Logger.logDebug(getClass(), 
				String.format("ShoppingList was saved in the local DB. [%s]", sl));
		
		return sl;
	}
	
	/**
	 * Removes the given Shopping list from the local DB
	 * @param sl - shopping list to be removed
	 */
	public void delete(ShoppingList sl) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to delete ShoppingList [%s]", sl));
		
		SQLiteDatabase db = new DBHelper().getWritableDatabase();
		
		int deleted = db.delete(TABLE_NAME, COLUMN_ID + "=" + sl.getId(), null);
		
		Logger.logDebug(getClass(), String.format("[%d] " +
				"ShoppingList records were deleted from the DB", deleted));
	}
	
	/**
	 * Removes all the shopping lists from the database
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
