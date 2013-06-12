/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 4, 2013
 */
public class DBAdaptor {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	
	/** Log tag - class identification */
	private static final String TAG = DBAdaptor.class.getName();
	
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	
	/** Android context */
	private Context ctx;
	
	/** Customer DB helper class */
	private DBHelper dbHelper;
	
	/** Database object */
	private SQLiteDatabase db;
	

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	
	/**
	 * Constructor. 
	 * @param ctx - android context
	 */
	public DBAdaptor(Context ctx) {
		
		this.ctx = ctx;
		
	}//DBAdaptor

	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	/**
	 * Opening the database.
	 * @return this object
	 */
	public DBAdaptor open() {
		
		//---------------------------------------------------
		// Create the writable DB object
		//---------------------------------------------------
		dbHelper = new DBHelper(ctx, this);
		if (db == null || !db.isOpen()) {
			db = dbHelper.getWritableDatabase();
		}//if
		
		Log.d(TAG, "DB has been opened");
		
		return this;
		
	}//open
	
	
	/**
	 * Close the database.
	 */
	public void close() {
		
		dbHelper.close();
		
		Log.d(TAG, "DB has been closed");
		
	}//close
	
	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
