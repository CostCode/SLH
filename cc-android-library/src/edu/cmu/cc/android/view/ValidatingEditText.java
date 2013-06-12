/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import edu.cmu.cc.android.view.validation.IViewValidator;
import edu.cmu.cc.android.view.validation.textview.MembershipValidator;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 12, 2013
 */
public class ValidatingEditText extends EditText implements IValidatingView {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	private IViewValidator validator;
	
	private String fieldDisplayName;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ValidatingEditText(Context ctx) {
		super(ctx);
	}
	
	public ValidatingEditText(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
	}
	
	public ValidatingEditText(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
	}
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void setValidator(IViewValidator validator, 
			String fieldDisplayName) {
		
		this.validator = validator;
		this.fieldDisplayName = fieldDisplayName;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void flagOrUnflagValidationError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unflagValidationError() {
		// TODO Auto-generated method stub
		
	}
	
	//-------------------------------------------------------------------------
	// PROTECTED METHODS
	//-------------------------------------------------------------------------

	protected void initInputType(IViewValidator validator) {
		
		if (validator instanceof MembershipValidator) {
			setInputType(InputType.TYPE_CLASS_NUMBER);
		} else {
			setInputType(InputType.TYPE_CLASS_TEXT);
		}
		
		
	}
	
	//-------------------------------------------------------------------------
	// HELPER METHODS
	//-------------------------------------------------------------------------

}
