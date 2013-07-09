/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.model.ItemCategory;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 21, 2013
 */
public class ItemCategoryDAO {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** TABLE NAME */
	static final String TABLE_NAME = "itemcategory";
	
	/** PRIMARY KEY: Item category id */
	static final String COLUMN_ID = "id";
	
	/** COLUMN: Item category name */
	static final String COLUMN_NAME = "name";
	
	/** COLUMN: Item category description */
	static final String COLUMN_DESC = "description";
	
	
	/** Create ItemCategory table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT UNIQUE, " +
			COLUMN_DESC + " TEXT)";
	
	static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ItemCategoryDAO() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Retrieves all item categories from the database
	 * @return - item categories list
	 */
	public List<ItemCategory> getAll() {
		
		SQLiteDatabase db = new DBHelper().getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, 
				COLUMN_NAME);
		
		int rowCount = cursor.getCount();
		Logger.logDebug(getClass(), 
				String.format("Retrieved %d Item Categories...", rowCount));
		
		List<ItemCategory> list = new ArrayList<ItemCategory>(rowCount);
		while(cursor.moveToNext()) {
			ItemCategory category = new ItemCategory();
			
			category.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
			category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
			category.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
			
			list.add(category);
		}
		
		cursor.close();
		db.close();
		
		return list;
	}
	
	/**
	 * Saves given item category object into the database.
	 * @param category - item category object to be saved
	 * @return saved item category object with attached id number
	 */
	public ItemCategory save(ItemCategory category) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save ItemCategory [%s]", category));
		
		SQLiteDatabase db = new DBHelper().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, category.getName());
		values.put(COLUMN_DESC, category.getDescription());
		
		if (category.getId() > 0) {
			db.update(TABLE_NAME, values, COLUMN_ID + "=" + category.getId(), null);
		} else {
			long id = db.insert(TABLE_NAME, null, values);
			category.setId(id);
		}
		
		Logger.logDebug(getClass(), 
				String.format("ItemCategory was saved in the local DB. [%s]", 
						category));
		
		return category;
	}
	
	/**
	 * Removes all the item categories from the database
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
