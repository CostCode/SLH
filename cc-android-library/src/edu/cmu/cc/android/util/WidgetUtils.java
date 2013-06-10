/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.util;

import android.view.View;
import android.widget.EditText;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 10, 2013
 */
public class WidgetUtils {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Get the text of the EditText by parent view and id
	 * @param parentView
	 * @param id - id of the widget
	 * @return text of the requested EditText
	 */
	public static String getEditTextAsString(View parentView, int id) {
		return getEditText(parentView, id).getText().toString();
	}
	
	/**
	 * Retrieve EditText widget from the parent view
	 * @param parentView
	 * @param id - id of the widget
	 * @return Requested widget
	 */
	public static EditText getEditText(View parentView, int id) {
		return (EditText) parentView.findViewById(id);
	}

	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
