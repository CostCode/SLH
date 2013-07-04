/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.android.util.WidgetUtils;
import edu.cmu.cc.android.view.IValidatingView;
import edu.cmu.cc.android.view.validation.IViewValidator;
import edu.cmu.cc.android.view.validation.textview.RegexValidator;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.ShoppingList;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 27, 2013
 */
public class ShoppingListViewAdapter {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private List<IValidatingView> validatingViews;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ShoppingListViewAdapter(View view) {
		
		initializeValidators(view);
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	public static void updateView(View view) {
		
		ShoppingList sl = 
				ApplicationState.getInstance().getShoppingList();
		
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
				ApplicationState.getInstance().getShoppingList();
		
		sl.setName(WidgetUtils
				.getEditTextAsString(view, R.id.etShoppingListName));
		
		sl.setDescription(WidgetUtils
				.getEditTextAsString(view, R.id.etShoppingListComments));
		
		ApplicationState.getInstance().setShoppingList(sl);
	}
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Performs validation on all views
	 */
	public void validateAllViews() {
		
		synchronized (validatingViews) {
			for (IValidatingView view : validatingViews) {
				view.flagOrUnflagValidationError();
			}
		}
	}
	
	/**
	 * Checks whether all the views are valid or not
	 * @return
	 */
	public boolean areAllViewsValid() {
		
		synchronized (validatingViews) {
			for (IValidatingView view : validatingViews) {
				if (!view.isValid()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Resets the values of the views and removes validation errors
	 */
	public void resetAllViewsValues() {
		
		synchronized (validatingViews) {
			for (IValidatingView view : validatingViews) {
				if (view instanceof EditText) {
					((EditText) view).setText("");
				}
				view.unflagValidationError();
			}
		}
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
		validatingViews = new ArrayList<IValidatingView>(1);
		
		synchronized (validatingViews) {
			assignValidatorToView(parentView, R.id.etShoppingListName, 
					R.string.shoppinglist_name, 
					new RegexValidator(ctx, ".{3,10}", R.string.validation_shoppinglist_name));
			assignValidatorToView(parentView, R.id.etShoppingListComments, 
					R.string.shoppinglist_comments, 
					new RegexValidator(ctx, ".{0,20}", R.string.validation_shoppinglist_comment));
		}
	}
	
	/**
	 * Assigns a given validator to the view
	 * @param parentView
	 * @param viewResID
	 * @param viewDisplayNameResID
	 * @param validator
	 */
	private void assignValidatorToView(View parentView, int viewResID, 
			int viewDisplayNameResID, IViewValidator validator) {
		
		IValidatingView validatingView = 
				(IValidatingView) parentView.findViewById(viewResID);
		
		String viewDisplayName = parentView.getContext().getResources()
				.getString(viewDisplayNameResID);
		
		validatingView.setValidator(validator, viewDisplayName);
		
		validatingViews.add(validatingView);
	}

}