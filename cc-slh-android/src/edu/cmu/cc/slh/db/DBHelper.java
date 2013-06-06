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
	
	
	/** Create User table SQL script */
	public static final String SQL_CREATE_USER =
			"CREATE TABLE " + DBContract.UserTable.TABLE_NAME + "(" +
			DBContract.UserTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DBContract.UserTable.MEMBER_ID + " TEXT, " +
			DBContract.UserTable.USERNAME + " TEXT, " +
			DBContract.UserTable.FIRSTNAME + " TEXT, " +
			DBContract.UserTable.LASTNAME + " TEXT, " +
			DBContract.UserTable.PASSWORD + " TEXT)";
	
	
	/** Create ItemCategory table SQL script */
	public static final String SQL_CREATE_ITEMCATEGORY =
			"CREATE TABLE " + DBContract.ItemCategoryTable.TABLE_NAME + "(" +
			DBContract.ItemCategoryTable.ID + 
			" INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DBContract.ItemCategoryTable.CATEGORY + " INTEGER, " +
			DBContract.ItemCategoryTable.NAME + " TEXT, " +
			DBContract.ItemCategoryTable.DESCRIPTION + " TEXT)";
	
	
	/** Create ShoppingList table SQL script */
	public static final String SQL_CREATE_SHOPPINGLIST =
			"CREATE TABLE " + DBContract.ShoppingListTable.TABLE_NAME + "(" +
			DBContract.ShoppingListTable.ID + 
			" INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DBContract.ShoppingListTable.OWNER + " INTEGER, " +
			DBContract.ShoppingListTable.NAME + " TEXT, " +
			DBContract.ShoppingListTable.DATE + " INTEGER, " +
			DBContract.ShoppingListTable.STATUS + " INTEGER, " +
			DBContract.ShoppingListTable.DESCRIPTION + " TEXT)";
	
	
	/** Create ShoppingListItem table SQL script */
	public static final String SQL_CREATE_SHOPPINGLISTITEM =
			"CREATE TABLE " + DBContract.ShoppingListItemTable.TABLE_NAME + "(" +
			DBContract.ShoppingListItemTable.ID + 
			" INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DBContract.ShoppingListItemTable.SHOPPING_LIST + " INTEGER, " +
			DBContract.ShoppingListItemTable.CATEGORY + " INTEGER, " +
			DBContract.ShoppingListItemTable.AMOUNT + " INTEGER, " +
			DBContract.ShoppingListItemTable.DESCRIPTION + " TEXT)";
	
	
	/** Drop table SQL script */
	public static final String SQL_DROP_TABLE =
			"DROP TABLE IF EXISTS ";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	
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
		db.execSQL(SQL_CREATE_USER);
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
				DBContract.ShoppingListItemTable.TABLE_NAME);
		db.execSQL(SQL_DROP_TABLE + 
				DBContract.ShoppingListTable.TABLE_NAME);
		db.execSQL(SQL_DROP_TABLE + 
				DBContract.ItemCategoryTable.TABLE_NAME);
		db.execSQL(SQL_DROP_TABLE + DBContract.UserTable.TABLE_NAME);
		
		Log.d(TAG, "Database tables have been deleted");
		
		onCreate(db);
		
	}//onUpgrade
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
