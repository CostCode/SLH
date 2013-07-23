/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dao;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.model.DataVersion;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 15, 2013
 */
@SuppressLint("DefaultLocale")
public class DataVersionDAO extends BaseDAO {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** TABLE NAME */
	static final String TABLE_NAME = "dataversion";
	
	/** PRIMARY KEY: Data version id */
	static final String COLUMN_ID = "id";
	
	/** COLUMN: Data name */
	static final String COLUMN_NAME = "name";
	
	/** COLUMN: Data version number */
	static final String COLUMN_VERSION = "version";
	
	/** COLUMN: Data version number update date */
	static final String COLUMN_DATE = "updatedate";
	
	/** COLUMN: Data version description */
	static final String COLUMN_DESC = "description";
	
	
	/** Create ShoppingListItem table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT UNIQUE, " +
			COLUMN_VERSION + " INTEGER, " +
			COLUMN_DATE + " INTEGER, " +
			COLUMN_DESC + " TEXT)";
	
	static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public DataVersionDAO() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Retrieves DataVersion object by the give data id
	 * @param dataId - data id. (e.g. DataVersion.DATA_AP)
	 * @return DataVersion object
	 */
	public DataVersion getVersion(final int dataId) {
		
		Cursor cursor = null;
		DataVersion dataVersion = null;
		
		try {
			
			if (db == null || !db.isOpen()) {
				db = new DBHelper().getWritableDatabase();
			}
			
			final String sqlWhere = String.format("%s=%d", COLUMN_ID, dataId);
			
			cursor = db.query(TABLE_NAME, null, sqlWhere, null, null, null, null);
			
			int rowCount = cursor.getCount();
			
			Logger.logDebug(getClass(), String
					.format("Retrieved [%d] DataVersion objects...", rowCount));
			
			if (rowCount == 0) {
				throw new IllegalStateException(String
						.format("No DataVersion found for [ID=%d] ", dataId));
			}
			
			if (cursor.moveToNext()) {
				
				dataVersion = new DataVersion();
				
				dataVersion.setId(
						cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
				dataVersion.setName(
						cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
				dataVersion.setVersion(
						cursor.getInt(cursor.getColumnIndex(COLUMN_VERSION)));
				dataVersion.setUpdateDate(
						new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
				dataVersion.setDescription(
						cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
			}
		} catch (Throwable t) {
			if (db != null) {
				db.close();
			}
			Logger.logErrorAndThrow(getClass(), t);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return dataVersion;
	}
	
	/**
	 * Saves the given DataVersion object into the local DB
	 * @param dataVersion - object to be saved
	 * @return saved DataVersion with attached id number
	 */
	public DataVersion saveVersion(DataVersion dataVersion) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save DataVersion [%s] into " +
						"the local DB", dataVersion));
		
		if (dataVersion == null) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException("Saving null " +
							"DataVersion is not allowed"));
		}
		
		try {
			
			if (db == null || !db.isOpen()) {
				db = new DBHelper().getWritableDatabase();
			}
			
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME, dataVersion.getName());
			values.put(COLUMN_VERSION, dataVersion.getVersion());
			values.put(COLUMN_DATE, dataVersion.getUpdateDate().getTime());
			values.put(COLUMN_DESC, dataVersion.getDescription());
			
			if (dataVersion.getId() > 0) {
				db.update(TABLE_NAME, values, String
						.format("%s=%d", COLUMN_ID, dataVersion.getId()), null);
			} else {
				long id = db.insert(TABLE_NAME, null, values);
				dataVersion.setId(id);
			}
			
			Logger.logDebug(getClass(), String.format("DataVersion " +
					"was saved in the local DB. [%s]", dataVersion));
			
		} catch (Throwable t) {
			if (db != null) {
				db.close();
			}
			Logger.logErrorAndThrow(getClass(), t);
		}
		
		return dataVersion;
	}
	
	
	/**
	 * Removes all the shopping list items from the local DB
	 */
	public void deleteAll() {
		
		if (db == null || !db.isOpen()) {
			db = new DBHelper().getWritableDatabase();
		}
		
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}
