/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.db;

import edu.cmu.cc.slh.entity.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

	/**
	 * Add a user into the DB
	 * @param user - user to be added
	 * @return a user object with id
	 */
	public User addUser(User user) {
		
		ContentValues values = new ContentValues();
		values.put(DBContract.User.MEMBER_ID, user.getMemberId());
		values.put(DBContract.User.USERNAME, user.getUsername());
		values.put(DBContract.User.FIRSTNAME, user.getFirstName());
		values.put(DBContract.User.LASTNAME, user.getLastName());
		values.put(DBContract.User.PASSWORD, user.getPassword());
		
		long id = db.insert(DBContract.User.TABLE_NAME, null, values);
		user.setId(id);
		
		Log.d(TAG, String.format("User was added into DB: [%s]", user));
		
		return user;
		
	}//addUser
	
	/**
	 * Retrieve a user object by username and password
	 * @param username
	 * @param password
	 * @return user object
	 */
	public User findUser(String username, String password) {
		
		Log.d(TAG, String.format("Getting a User from local DB: " +
				"USERNAME=%s, PASSWORD=%s", username, password));
		
		Cursor cursor = db.query(DBContract.User.TABLE_NAME, 
				new String[] {
					DBContract.User.ID,
					DBContract.User.MEMBER_ID,
					DBContract.User.FIRSTNAME,
					DBContract.User.LASTNAME
				}, 
				DBContract.User.USERNAME + " = '" + username + "' AND " +
				DBContract.User.PASSWORD + " = '" + password + "'",
				null, null, null, null);
		
		if (cursor != null && cursor.getCount() > 0) {
			Log.d(TAG, String.format("User was found. Number of matches = %d", 
					cursor.getCount()) );
			cursor.moveToFirst();
		} else {
			Log.d(TAG, "No User was found by the given parameters");
			return null;
		}
		
		long id = cursor.getLong(
				cursor.getColumnIndexOrThrow(DBContract.User.ID)
		);
		String memberId = cursor.getString(
				cursor.getColumnIndexOrThrow(DBContract.User.MEMBER_ID)
		);
		String firstName = cursor.getString(
				cursor.getColumnIndexOrThrow(DBContract.User.FIRSTNAME)
		);
		String lastName = cursor.getString(
				cursor.getColumnIndexOrThrow(DBContract.User.LASTNAME)
		);
		
		User user = new User(id, memberId, firstName, lastName, 
				username, password);
		
		return user;
		
	}
	
	
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
