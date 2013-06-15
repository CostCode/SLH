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

import edu.cmu.cc.android.util.WidgetUtils;
import edu.cmu.cc.android.view.IValidatingView;
import edu.cmu.cc.android.view.validation.IViewValidator;
import edu.cmu.cc.android.view.validation.textview.MembershipValidator;
import edu.cmu.cc.slh.R;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 14, 2013
 */
public class ActivationViewAdapter {

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
	
	public ActivationViewAdapter(View activationView) {
		
		initializeValidators(activationView);
	}

	//-------------------------------------------------------------------------
	// STATIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Returns the value of the membership id edit text from the view
	 * @param activationView - parent view
	 * @return string value
	 */
	public static String getMembershipID(View activationView) {
		
		String membershipID = WidgetUtils.getEditTextAsString(activationView, 
				R.id.etActivationMembershipID);
		
		return membershipID;
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
			assignValidatorToView(parentView, R.id.etActivationMembershipID, 
					R.string.activation_membershipid, 
					new MembershipValidator(ctx));
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
