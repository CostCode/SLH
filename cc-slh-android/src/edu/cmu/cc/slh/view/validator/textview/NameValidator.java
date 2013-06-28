/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.view.validator.textview;

import android.content.Context;
import edu.cmu.cc.android.view.validation.textview.TextValidator;
import edu.cmu.cc.slh.R;

/**
 *  DESCRIPTION: Custom validator that validates Costco membership id.
 *  It uses RegexValidator as base.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 10, 2013
 */
public class NameValidator extends TextValidator {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	/** Name pattern */
	private static final String REGEX = "^[a-z0-9_- ]{3,20}$";
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * @param ctx - android context
	 */
	public NameValidator(Context ctx) {
		super(ctx, REGEX);
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public String getErrorMessage(String caption) {
		return ctx.getString(R.string.validation_name, caption);
	}
	
}