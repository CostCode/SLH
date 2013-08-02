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
import edu.cmu.cc.slh.model.BaseEntity;
import edu.cmu.cc.slh.model.Warehouse;

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
	
	
	/** COLUMN: Warehouse to which this access point belongs */
	static final String COLUMN_WAREHOUSE = "warehouseId";
	
	/** COLUMN: Access point SSID */
	static final String COLUMN_SSID = "ssid";
	
	/** COLUMN: Access point X position */
	static final String COLUMN_POSX = "posx";
	
	/** COLUMN: Access point Y position */
	static final String COLUMN_POSY = "posy";
	
	
	/** Create AccessPoint table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY, " +
			COLUMN_WAREHOUSE + " INTEGER, " +
			COLUMN_SSID + " TEXT, " +
			COLUMN_POSX + " FLOAT, " +
			COLUMN_POSY + " FLOAT, " +
			"FOREIGN KEY (" + COLUMN_WAREHOUSE + ") REFERENCES " +
			WarehouseDAO.TABLE_NAME + "(" + WarehouseDAO.COLUMN_ID + ") " +
			"ON DELETE CASCADE)";
	
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
	
	public List<AccessPoint> getAll(Warehouse wh) {
		
		if (!super.isValid(wh)) {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException(String.format("Warehouse[%s]" +
							" has wrong value", wh)));
		}
		
		Cursor cursor = null;
		List<AccessPoint> list = null;
		
		try {
			
			openConnectionIfClosed();
			
			cursor = db.query(TABLE_NAME, null, 
					String.format("%s=%d", COLUMN_WAREHOUSE, wh.getId()), 
					null, null, null, null);
			
			int rowCount = cursor.getCount();
			Logger.logDebug(getClass(), String
					.format("Retrieved [%d] AccessPoint objects...", rowCount));
			
			list = new ArrayList<AccessPoint>(rowCount);
			
			while(cursor.moveToNext()) {
				AccessPoint ap = new AccessPoint();
				
				ap.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
				ap.setWarehouse(wh);
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
	public void save(AccessPoint ap) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save AccessPoint [%s]", ap));
		
		if (!isValid(ap)) {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException(String.format("Section[%s]" +
							" has wrong value", ap)));
		}
		
		Cursor cursor = null;
		
		try {
			
			openConnectionIfClosed();
			
			ContentValues values = new ContentValues();
			values.put(COLUMN_SSID, ap.getSsid());
			values.put(COLUMN_POSX, ap.getPosX());
			values.put(COLUMN_POSY, ap.getPosY());
			
			if (alreadyExists(TABLE_NAME, ap, cursor)) {
				db.update(TABLE_NAME, values, 
						String.format("%s=%d", COLUMN_ID, ap.getId()), null);
			} else {
				values.put(COLUMN_ID, ap.getId());
				db.insert(TABLE_NAME, null, values);
			}
			
			Logger.logDebug(getClass(), String.format("AccessPoint[%s] " +
					"was saved into the local DB", ap));
			
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
	
	public void deleteAll(Warehouse wh) {
		
		Logger.logDebug(getClass(), String
				.format("Trying to delete AccessPoints of Warehouse: [%s]", wh));
		
		if (!super.isValid(wh)) {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException(String.format("Warehouse[%s]" +
							" has wrong value", wh)));
		}
		
		try {
			
			openConnectionIfClosed();
			
			int deleted = db.delete(TABLE_NAME, 
					COLUMN_WAREHOUSE + "=" + wh.getId(), null);
			
			Logger.logDebug(getClass(), String.format("[%d] " +
					"AccessPoints were deleted from the DB", deleted));
			
		} catch (Throwable t) {
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
	
	@Override
	protected boolean isValid(BaseEntity entity) {
		return super.isValid(entity) 
				&& super.isValid(((AccessPoint)entity).getWarehouse());
	}

}
