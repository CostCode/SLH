/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;

import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.android.util.WidgetUtils;
import edu.cmu.cc.android.view.IValidatingView;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.view.validator.textview.CommentValidator;
import edu.cmu.cc.slh.view.validator.textview.NameValidator;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 27, 2013
 */
public class SLViewAdapter extends AbstractViewAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final int VALIDATING_VIEWS_COUNT = 1;
	
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public SLViewAdapter(View view) {
		
		initializeValidators(view);
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static void updateView(View view) {
		
		ShoppingList sl = 
				ApplicationState.getInstance().getCurrentSL();
		
		WidgetUtils.getEditText(view, R.id.etShoppingListName)
			.setText(sl.getName());
		WidgetUtils.getEditText(view, R.id.etShoppingListComments)
			.setText(sl.getDescription());
		
		WidgetUtils.getTextView(view, R.id.tvShoppingListDate)
			.setText(StringUtils.getDateAsString(sl.getDate(), DATE_PATTERN));
		WidgetUtils.getTextView(view, R.id.tvShoppingListDate)
			.setTag(sl.getDate());
	}
	
	
	public static void updateModel(View view) {
		
		ShoppingList sl = 
				ApplicationState.getInstance().getCurrentSL();
		
		sl.setName(WidgetUtils
				.getEditTextAsString(view, R.id.etShoppingListName));
		
		sl.setDescription(WidgetUtils
				.getEditTextAsString(view, R.id.etShoppingListComments));
		
		ApplicationState.getInstance().setCurrentSL(sl);
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Initializes validators and sets them to all validating views
	 * @param parentView - parent view
	 */
	private void initializeValidators(View parentView) {
		
		Context ctx = parentView.getContext();
		validatingViews = 
				new ArrayList<IValidatingView>(VALIDATING_VIEWS_COUNT);
		
		synchronized (validatingViews) {
			assignValidatorToView(parentView, R.id.etShoppingListName, 
					R.string.sl_name, new NameValidator(ctx));
			assignValidatorToView(parentView, R.id.etShoppingListComments, 
					R.string.sl_comment, new CommentValidator(ctx));
		}
	}

}