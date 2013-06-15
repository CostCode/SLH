/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
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
		
		initInputType(validator);
		registerListerners();
	}

	@Override
	public boolean isValid() {
		return validator.validate(this);
	}

	@Override
	public void flagOrUnflagValidationError() {
		String errMsg = (isValid()) ? null : 
			validator.getErrorMessage(fieldDisplayName);
		setError(errMsg);
	}

	@Override
	public void unflagValidationError() {
		setError(null);
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
		setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	}
	
	/**
	 * Setting up listeners for this view.
	 */
	protected void registerListerners() {
		registerOnFocusChangeListener();
		registerOnKeyListener();
		registerOnLongClickListener();
	}
	
	protected void registerOnFocusChangeListener() {
		setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					flagOrUnflagValidationError();
				}
			}
		});
	}
	
	protected void registerOnKeyListener() {
		setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				final int KEY_CODE_ENTER = 66;
				boolean consumed = false;
				if (keyCode == KEY_CODE_ENTER && 
						event.getAction() == KeyEvent.ACTION_DOWN) {
					flagOrUnflagValidationError();
					consumed = true;
				}
				return consumed;
			}
		});
	}
	
	protected void registerOnLongClickListener() {
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				flagOrUnflagValidationError();
				return true;
			}
		});
	}

}
