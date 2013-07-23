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
import edu.cmu.cc.slh.model.AccessPoint;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: July 15, 2013
 */
public class AccessPointDAO extends BaseDAO {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** TABLE NAME */
	static final String TABLE_NAME = "accesspoint";
	
	/** PRIMARY KEY: Access point id */
	static final String COLUMN_ID = "id";
	
	/** COLUMN: Access point BSSID */
	static final String COLUMN_BSSID = "bssid";
	
	/** COLUMN: Access point SSID */
	static final String COLUMN_SSID = "ssid";
	
	/** COLUMN: Access point X position */
	static final String COLUMN_POSX = "posx";
	
	/** COLUMN: Access point Y position */
	static final String COLUMN_POSY = "posy";
	
	/** COLUMN: Access point description */
	static final String COLUMN_DESC = "description";
	
	
	/** Create AccessPoint table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_BSSID + " TEXT, " +
			COLUMN_SSID + " TEXT, " +
			COLUMN_POSX + " FLOAT, " +
			COLUMN_POSY + " FLOAT, " +
			COLUMN_DESC + " TEXT)";
	
	static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public AccessPointDAO() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	
	
	/**
	 * Retrieves all the access points 
	 * @return list of AccessPoint objects
	 */
	public List<AccessPoint> getAll() {
		
		Cursor cursor = null;
		List<AccessPoint> list = null;
		
		try {
			
			if (db == null || !db.isOpen()) {
				db = new DBHelper().getWritableDatabase();
			}
			
			cursor = db.query(true, TABLE_NAME, null, null, null, null, null, 
					null, null, null);
			
			int rowCount = cursor.getCount();
			Logger.logDebug(getClass(), String
					.format("Retrieved [%d] AccessPoint objects...", rowCount));
			
			if (rowCount == 0) {
				return null;
			}
			
			list = new ArrayList<AccessPoint>(rowCount);
			
			while(cursor.moveToNext()) {
				AccessPoint ap = new AccessPoint();
				
				ap.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
				
				ap.setBssid(cursor.getString(cursor.getColumnIndex(COLUMN_BSSID)));
				
				ap.setSsid(cursor.getString(cursor.getColumnIndex(COLUMN_SSID)));
				
				ap.setPosX(cursor.getFloat(cursor.getColumnIndex(COLUMN_POSX)));
				
				ap.setPosY(cursor.getFloat(cursor.getColumnIndex(COLUMN_POSY)));
				
				list.add(ap);
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
	
	/**
	 * Saves the given AccessPoing into the local DB
	 * @param ap - AccessPoint object to be saved
	 * @return saved AccessPoint with attached id number
	 */
	public AccessPoint save(AccessPoint ap) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save AccessPoint [%s] into " +
						"the local DB", ap));
		
		if (ap == null) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException("Saving null " +
							"AccessPoint object is not allowed"));
		}
		
		try {
			
			if (db == null || !db.isOpen()) {
				db = new DBHelper().getWritableDatabase();
			}
			
			ContentValues values = new ContentValues();
			values.put(COLUMN_BSSID, ap.getBssid());
			values.put(COLUMN_SSID, ap.getSsid());
			values.put(COLUMN_POSX, ap.getPosX());
			values.put(COLUMN_POSY, ap.getPosY());
			values.put(COLUMN_DESC, ap.getDescription());
			
			if (ap.getId() > 0) {
				db.update(TABLE_NAME, values, 
						String.format("%s=%d", COLUMN_ID, ap.getId()), null);
			} else {
				long id = db.insert(TABLE_NAME, null, values);
				ap.setId(id);
			}
			
			Logger.logDebug(getClass(), String.format("AccessPoint " +
					"was saved in the local DB. [%s]", ap));
			
		} catch (Throwable t) {
			if (db != null) {
				db.close();
			}
			Logger.logErrorAndThrow(getClass(), t);
		}
		
		return ap;
	}
	
	/**
	 * Saves the given list of AccessPoints into the local DB
	 * @param apList - AccessPoint list
	 */
	public void saveAll(List<AccessPoint> apList) {
		
		if (apList == null || apList.size() == 0) {
			Logger.logDebug(getClass(), "Access points list is empty or null");
			return;
		}
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save AccessPoints list [Size=%d] " +
						"into the local DB", apList.size()));
		
		try {
			
			if (db == null || !db.isOpen()) {
				db = new DBHelper().getWritableDatabase();
			}
			
			for (AccessPoint ap : apList) {
				save(ap);
			}
			
			Logger.logDebug(getClass(), String.format("AccessPoint list was" +
					"saved in the local DB"));
			
		} catch (Throwable t) {
			if (db != null) {
				db.close();
			}
			Logger.logErrorAndThrow(getClass(), t);
		}
	}
	
	/**
	 * Removes all the shopping list items from the local DB
	 */
	public void deleteAll() {
		
		if (db == null || !db.isOpen()) {
			db = new DBHelper().getWritableDatabase();
		}
		
		db.delete(TABLE_NAME, null, null);
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}
