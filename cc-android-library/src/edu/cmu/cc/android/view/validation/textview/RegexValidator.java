/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.android.view.validation.textview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import edu.cmu.cc.android.view.validation.IViewValidator;

/**
 *  DESCRIPTION: Common regular expression validator.
 *  Other custom validators can extend this class.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 10, 2013
 */
public abstract class RegexValidator implements IViewValidator {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	/** Android Context */
	protected Context ctx;
	
	/** Regular expression pattern */
	protected Pattern pattern;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor. Sets the context and regex to this validator.
	 * @param ctx - android context
	 * @param pattern - regular expression to validate against
	 */
	public RegexValidator(Context ctx, String regex) {
		this.ctx = ctx;
		this.pattern = Pattern.compile(regex);
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public boolean validate(View view) {
		Matcher matcher = pattern.matcher(((TextView)view).getText());
		return matcher.matches();
	}

//	@Override
//	public String getErrorMessage(String caption) {
//		return ctx.getString(R.string.validation_regex, caption, 
//				pattern.toString());
//	}
	
}
