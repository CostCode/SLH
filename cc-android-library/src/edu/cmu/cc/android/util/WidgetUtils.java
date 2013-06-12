/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.util;

import edu.cmu.cc.android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.EditText;

/**
 *  DESCRIPTION: Helper class for managing Android UI widgets
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 10, 2013
 */
public class WidgetUtils {

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
	
	
	public static AlertDialog createOkAlertDialog(Context ctx, int iconResID, 
			int titleResID, String message) {
		
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		
		return createOkAlertDialog(ctx, iconResID, titleResID, message, listener);
		
	}
	
	public static AlertDialog createOkAlertDialog(Context ctx, int iconResID, 
			int titleResID, String message, OnClickListener listener) {
		
		final AlertDialog dialog = createOkAlertDialog(ctx, iconResID, 
				titleResID, message, R.string.androidUtil_ok, listener);
		
		return dialog;
	}
	
	public static AlertDialog createOkAlertDialog(Context ctx, int iconResID, 
			int titleResID, String message, int okButtonTextResID, 
			OnClickListener listener) {
		
		final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
		
		dialog.setIcon(iconResID);
		dialog.setTitle(titleResID);
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, 
				ctx.getString(okButtonTextResID), listener);
		
		return dialog;
	}

}
