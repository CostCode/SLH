/**
 * Copyright (c) 2013, Azamat Samiyev. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 4, 2013
 */
public final class DBHelper extends SQLiteOpenHelper {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	/** Log tag - class identification */
	private static final String TAG = DBHelper.class.getName();
	
	
	/** Database version number. Used to check for updates */
	public static final int DATABASE_VERSION = 0;
	
	
	/** Database name */
	public static final String DATABASE_NAME = "cc.slh.db";
	
	
	/** Create ItemCategory table SQL script */
	public static final String SQL_CREATE_ITEMCATEGORY =
			"CREATE TABLE " + DBContract.ItemCategory.TABLE_NAME + "(" +
			DBContract.ItemCategory.ID + 
			" INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DBContract.ItemCategory.CATEGORY + " INTEGER, " +
			DBContract.ItemCategory.NAME + " TEXT UNIQUE, " +
			DBContract.ItemCategory.DESCRIPTION + " TEXT)";
	
	
	/** Create ShoppingList table SQL script */
	public static final String SQL_CREATE_SHOPPINGLIST =
			"CREATE TABLE " + DBContract.ShoppingList.TABLE_NAME + "(" +
			DBContract.ShoppingList.ID + 
			" INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DBContract.ShoppingList.NAME + " TEXT UNIQUE, " +
			DBContract.ShoppingList.DATE + " INTEGER, " +
			DBContract.ShoppingList.DESCRIPTION + " TEXT";
	
	
	/** Create ShoppingListItem table SQL script */
	public static final String SQL_CREATE_SHOPPINGLISTITEM =
			"CREATE TABLE " + DBContract.ShoppingListItem.TABLE_NAME + "(" +
			DBContract.ShoppingListItem.ID + 
			" INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DBContract.ShoppingListItem.SHOPPING_LIST + " INTEGER, " +
			DBContract.ShoppingListItem.CATEGORY + " INTEGER, " +
			DBContract.ShoppingListItem.QUANTITY + " INTEGER, " +
			DBContract.ShoppingListItem.UNIT + " INTEGER, " +
			DBContract.ShoppingListItem.DESCRIPTION + " TEXT, " +
			"FOREIGN KEY(" + 
			DBContract.ShoppingListItem.SHOPPING_LIST +
			") REFERENCES " +
			DBContract.ShoppingList.TABLE_NAME + "(" +
			DBContract.ShoppingList.ID + "), " +
			"FOREIGN KEY(" + 
			DBContract.ShoppingListItem.CATEGORY +
			") REFERENCES " +
			DBContract.ItemCategory.TABLE_NAME + "(" +
			DBContract.ItemCategory.ID + "))";
	
	
	/** Drop table SQL script */
	public static final String SQL_DROP_TABLE =
			"DROP TABLE IF EXISTS ";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//TODO: Finish dbAdaptor code!!!
	private DBAdaptor dbAdaptor;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	/**
	 * Constructor. 
	 * @param ctx - android context
	 * @param dbAdaptor - database adaptor object.
	 */
	public DBHelper(Context ctx, DBAdaptor dbAdaptor) {
		
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.dbAdaptor = dbAdaptor;
		
	}//DBHelper
	
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	/**
	 * Create all DB tables.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("PRAGMA foreign_keys=ON;");
		db.execSQL(SQL_CREATE_ITEMCATEGORY);
		db.execSQL(SQL_CREATE_SHOPPINGLIST);
		db.execSQL(SQL_CREATE_SHOPPINGLISTITEM);
		
		Log.d(TAG, "DB tables have been created...");
		
	}//onCreate

	/**
	 * If the DB version is updated, delete all tables.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.i(TAG, String.format("Upgrading DB from version %d to version %d", 
				oldVersion, newVersion));
		
		db.execSQL(SQL_DROP_TABLE + 
				DBContract.ShoppingListItem.TABLE_NAME);
		db.execSQL(SQL_DROP_TABLE + 
				DBContract.ShoppingList.TABLE_NAME);
		db.execSQL(SQL_DROP_TABLE + 
				DBContract.ItemCategory.TABLE_NAME);
		
		Log.d(TAG, "Database tables have been deleted");
		
		onCreate(db);
		
	}//onUpgrade
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
