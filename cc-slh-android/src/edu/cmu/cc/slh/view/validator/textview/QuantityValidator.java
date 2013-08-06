/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.view.validator.textview;

import android.content.Context;
import edu.cmu.cc.android.view.validation.textview.IntegerNumberValidator;
import edu.cmu.cc.slh.R;

/**
 *  DESCRIPTION: Custom validator which validates quantity number. Valid
 *  quantity number should be within the range of 0..999
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 10, 2013
 */
public class QuantityValidator extends IntegerNumberValidator {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	/** Number range: 0-999 */
	private static final String REGEX = "^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])$";
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * @param ctx - android context
	 */
	public QuantityValidator(Context ctx) {
		super(ctx, REGEX);
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public String getErrorMessage(String caption) {
		return ctx.getString(R.string.validation_quantity, caption);
	}
	
}
