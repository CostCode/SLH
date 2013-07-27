/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.model.BaseEntity;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.Section;
import edu.cmu.cc.slh.model.Warehouse;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 24, 2013
 */
public class SectionDAO extends BaseDAO {

	class SectionCategory {
		
		/** TABLE NAME */
		static final String TABLE_NAME = "sectionCategory";
		
		/** PRIMARY KEY: Section id */
		static final String COLUMN_SECTION = "sectionId";
		
		/** PRIMARY KEY: Item category id */
		static final String COLUMN_CATEGORY = "categoryId";
		
		/** Create sectionCategory table SQL script */
		static final String SQL_CREATE_TABLE =
				"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
				COLUMN_SECTION + " INTEGER, " +
				COLUMN_CATEGORY + " INTEGER, " +
				"FOREIGN KEY (" + COLUMN_SECTION + ") REFERENCES " +
				SectionDAO.TABLE_NAME + "(" + SectionDAO.COLUMN_ID + ") " +
				"ON DELETE CASCADE, " +
				"FOREIGN KEY (" + COLUMN_CATEGORY + ") REFERENCES " +
				ItemCategoryDAO.TABLE_NAME + "(" + ItemCategoryDAO.COLUMN_ID + ") " +
				"ON DELETE CASCADE)";
		
		static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		
	}
	
	
	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	/** TABLE NAME */
	static final String TABLE_NAME = "section";
	
	/** COLUMN: Section Warehouse */
	static final String COLUMN_WAREHOUSE = "warehouseId";
	
	/** COLUMN: Section X position */
	static final String COLUMN_POSX = "posx";
	
	/** COLUMN: Section Y position */
	static final String COLUMN_POSY = "posy";
	
	
	/** Create Section table SQL script */
	static final String SQL_CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY, " +
			COLUMN_WAREHOUSE + " INTEGER, " +
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
	
	public SectionDAO() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public List<Section> getAll(Warehouse wh) {
		
		if (!super.isValid(wh)) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException(String.format("Warehouse[%s]" +
							" has wrong value", wh)));
		}
		
		Cursor cursor = null;
		Cursor scCursor = null;
		List<Section> list = null;
		
		try {
			
			openConnectionIfClosed();
			
			cursor = db.query(TABLE_NAME, null, 
					String.format("%s=%d", COLUMN_WAREHOUSE, wh.getId()), 
					null, null, null, null);
			
			int rowCount = cursor.getCount();
			Logger.logDebug(getClass(), String
					.format("Retrieved [%d] Section objects...", rowCount));
			
			list = new ArrayList<Section>(rowCount);
			
			while(cursor.moveToNext()) {
				Section section = new Section();
				
				section.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
				section.setWarehouse(wh);
				section.setPosX(cursor.getDouble(cursor.getColumnIndex(COLUMN_POSX)));
				section.setPosY(cursor.getDouble(cursor.getColumnIndex(COLUMN_POSY)));
				
				//---------------------------------------------------
				// Getting SectionCategories for this section
				//---------------------------------------------------
				
				scCursor = getSectionCategories(section);
				rowCount = scCursor.getCount();
				Logger.logDebug(getClass(), String.format("Retrieved [%d] " +
						"SectionCategories objects...", rowCount));
				
				Map<Long,ItemCategory> categories = 
						ApplicationState.getInstance().getCategories();
				
				while(scCursor.moveToNext()) {
					
					long categoryId = scCursor.getLong(scCursor.getColumnIndex(
							SectionCategory.COLUMN_CATEGORY));
					
					ItemCategory category = categories.get(categoryId);
					section.addCategory(category);
				}
				
				list.add(section);
			}
			
		} catch (Throwable t) {
			if (db != null) {
				db.close();
			}
			Logger.logErrorAndThrow(getClass(), t);
		} finally {
			if (scCursor != null) {
				scCursor.close();
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return list;
	}
	
	public void save(Section section) {
		
		Logger.logDebug(getClass(), 
				String.format("Trying to save Section [%s]", section));
		
		if (!isValid(section)) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException(String.format("Section[%s]" +
							" has wrong value", section)));
		}
		
		Cursor cursor = null;
		
		try {
			
			openConnectionIfClosed();
			
			ContentValues values = new ContentValues();
			values.put(COLUMN_WAREHOUSE, section.getWarehouse().getId());
			values.put(COLUMN_POSX, section.getPosX());
			values.put(COLUMN_POSY, section.getPosY());
			
			if (alreadyExists(TABLE_NAME, section, cursor)) {
				db.update(TABLE_NAME, values, String.format("%s=%d", 
						COLUMN_ID, section.getId()), null);
			} else {
				values.put(COLUMN_ID, section.getId());
				db.insert(TABLE_NAME, null, values);
			}
			
			Logger.logDebug(getClass(), String.format("Section[%s] " +
					"was saved into the local DB", section));
			
			
			for (ItemCategory category : section.getCategories()) {
				saveSectionCategory(section, category);
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
	}
	
	public void deleteAll(Warehouse wh) {
		
		Logger.logDebug(getClass(), String
				.format("Trying to delete Sections of Warehouse: [%s]", wh));
		
		if (!super.isValid(wh)) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException(String.format("Warehouse[%s]" +
							" has wrong value", wh)));
		}
		
		try {
			
			openConnectionIfClosed();
			
			int deleted = db.delete(TABLE_NAME, 
					COLUMN_WAREHOUSE + "=" + wh.getId(), null);
			
			Logger.logDebug(getClass(), String.format("[%d] " +
					"Sections were deleted from the DB", deleted));
			
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
	
	private Cursor getSectionCategories(Section section) {
		
		if (!super.isValid(section)) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException(String.format("Section[%s]" +
							" has wrong value", section)));
		}
		
		Cursor cursor = db.query(SectionCategory.TABLE_NAME, null, 
				String.format("%s=%d", SectionCategory.COLUMN_SECTION, section.getId()), 
				null, null, null, null);
		
		return cursor;
	}
	
	
	private void saveSectionCategory(Section section, ItemCategory category) {
		
		if (!super.isValid(category)) {
			Logger.logErrorAndThrow(getClass(), 
					new RuntimeException(String.format("ItemCategory[%s]" +
							" has wrong value", category)));
		}
		
		Cursor cursor = null;
		
		try {
			
			openConnectionIfClosed();
			
			ContentValues values = new ContentValues();
			values.put(SectionCategory.COLUMN_SECTION, section.getId());
			values.put(SectionCategory.COLUMN_CATEGORY, category.getId());
			
			if (!existsSectionCategory(section, category, cursor)) {
				db.insert(SectionCategory.TABLE_NAME, null, values);
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
		
	}
	
	
	@Override
	protected boolean isValid(BaseEntity entity) {
		return super.isValid(entity) 
				&& super.isValid(((Section)entity).getWarehouse());
	}
	
	
	private boolean existsSectionCategory(Section section, 
			ItemCategory category, Cursor cursor) {
		
		if (!isValid(section) || !super.isValid(category)) {
			return false;
		}
		
		if (cursor == null || cursor.isClosed()) {
			cursor = db.query(SectionCategory.TABLE_NAME, null, 
					String.format("%s=%d AND %s=%d", 
							SectionCategory.COLUMN_SECTION, section.getId(),
							SectionCategory.COLUMN_CATEGORY, category.getId()), 
					null, null, null, null);
		}
		
		return (cursor.getCount() > 0);
	}


}
