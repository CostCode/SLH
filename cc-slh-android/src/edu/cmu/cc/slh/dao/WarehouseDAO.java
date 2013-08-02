/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.model.Warehouse;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class WarehouseDAO extends BaseDAO {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	/** TABLE NAME */
	static final String TABLE_NAME = "warehouse";
	
	/** COLUMN: Warehouse name */
	static final String COLUMN_ADDRESS = "address";
	
	/** COLUMN: Warehouse version number */
	static final String COLUMN_VERSION = "version";
	
	
	/** Create Warehouse table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY, " +
			COLUMN_ADDRESS + " TEXT, " +
			COLUMN_VERSION + " INTEGER)";
	
	static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public WarehouseDAO() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public List<Warehouse> getAll() {
		
		Cursor cursor = null;
		List<Warehouse> list = null;
		
		try {
			
			openConnectionIfClosed();
			
			cursor = db.query(TABLE_NAME, null, null, null, null, null, 
					COLUMN_ADDRESS);
			
			int rowCount = cursor.getCount();
			Logger.logDebug(getClass(), 
					String.format("Retrieved %d Warehouses...", rowCount));
			
			list = new ArrayList<Warehouse>(rowCount);
			
			while(cursor.moveToNext()) {
				Warehouse wh = new Warehouse();
				
				wh.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
				wh.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)));
				wh.setVersion(cursor.getInt(cursor.getColumnIndex(COLUMN_VERSION)));
				
				list.add(wh);
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
		
		return list;
	}
	
	public Warehouse getById(long id) {
		
		Cursor cursor = null;
		Warehouse wh = null;
		
		try {
			
			openConnectionIfClosed();
			
			cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=" + id, null, 
					null, null, null);
			
			int rowCount = cursor.getCount();
			Logger.logDebug(getClass(), 
					String.format("Retrieved %d Warehouses...", rowCount));
			
			if (cursor.moveToFirst()) {
				wh = new Warehouse();
				
				wh.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
				wh.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)));
				wh.setVersion(cursor.getInt(cursor.getColumnIndex(COLUMN_VERSION)));
			}
			
		}catch (Throwable t) {
			if (db != null) {
				db.close();
			}
			Logger.logErrorAndThrow(getClass(), t);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return wh;
	}
	
	public void save(Warehouse wh) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save Warehouse [%s]", wh));
		
		if (!isValid(wh)) {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalAccessException(String.format("Warehouse[%s]" +
							" has wrong value", wh)));
		}
		
		Cursor cursor = null;
		
		try {
			
			openConnectionIfClosed();
			
			ContentValues values = new ContentValues();
			values.put(COLUMN_ADDRESS, wh.getAddress());
			values.put(COLUMN_VERSION, wh.getVersion());
			
			if (alreadyExists(TABLE_NAME, wh, cursor)) {
				db.update(TABLE_NAME, values, COLUMN_ID + "=" + wh.getId(), null);
			} else {
				values.put(COLUMN_ID, wh.getId());
				db.insert(TABLE_NAME, null, values);
			}
			
			Logger.logDebug(getClass(), String.format("Warehouse[%s] " +
					"was saved into the local DB", wh));
			
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
		
	}
	
	public void delete(Warehouse wh) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to delete Warehouse [%s]", wh));
		
		if (!isValid(wh)) {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException(String.format("Warehouse[%s]" +
							" has wrong value", wh)));
		}
		
		try {
			
			openConnectionIfClosed();
			
			int deleted = 
					db.delete(TABLE_NAME, COLUMN_ID + "=" + wh.getId(), null);
			
			Logger.logDebug(getClass(), String.format("[%d] " +
					"Warehouse records were deleted from the DB", deleted));
			
		}  catch (Throwable t) {
			if (db != null) {
				db.close();
			}
			Logger.logErrorAndThrow(getClass(), t);
		}
		
	}
	
	public void deleteAll() {
		
		openConnectionIfClosed();
		
		db.delete(TABLE_NAME, null, null);
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}
