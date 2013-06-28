/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.view.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.android.util.WidgetUtils;
import edu.cmu.cc.android.view.IValidatingView;
import edu.cmu.cc.android.view.validation.IViewValidator;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.view.validator.textview.NameValidator;

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
	
	public static void updateView(View view, ShoppingList sl) {
		
		WidgetUtils.getEditText(view, R.id.etShoppingListName)
			.setText(sl.getName());
		WidgetUtils.getEditText(view, R.id.etShoppingListComments)
			.setText(sl.getDescription());
		
		WidgetUtils.getTextView(view, R.id.tvShoppingListDate)
			.setText(StringUtils.getDateAsString(sl.getDate(), "yyyy-MM-dd"));
		WidgetUtils.getTextView(view, R.id.tvShoppingListDate)
			.setTag(sl.getDate());
	}
	
	
	public static ShoppingList fromView(View shoppingListView) {
		
		String name = WidgetUtils.getEditTextAsString(shoppingListView, 
				R.id.etShoppingListName);
		
		String comments = WidgetUtils.getEditTextAsString(shoppingListView, 
				R.id.etShoppingListComments);
		
		Date date = (Date) WidgetUtils.getTextView(
				shoppingListView, R.id.tvShoppingListDate).getTag();
		
		return new ShoppingList(name, date, comments);
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
					new NameValidator(ctx));
			assignValidatorToView(parentView, R.id.etShoppingListComments, 
					R.string.shoppinglist_comments, 
					new NameValidator(ctx));
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