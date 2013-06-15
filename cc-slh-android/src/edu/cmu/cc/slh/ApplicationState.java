/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh;

import edu.cmu.cc.android.util.SharedPrefsAdapter;
import android.app.Application;


/**
 *  DESCRIPTION: This class is used to hold application data.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 10, 2013
 */
public class ApplicationState extends Application {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String ACTIVATED = "activated";

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ApplicationState() {}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public void setActivated(boolean activated) {
		
		SharedPrefsAdapter
			.persist(this, ACTIVATED, String.valueOf(activated));
	}
	
	public boolean isActivated() {
		
		String strActivated = 
				SharedPrefsAdapter.retrieve(this, ACTIVATED);
		
		return Boolean.parseBoolean(strActivated);
	}
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
