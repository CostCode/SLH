/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Aug 1, 2013
 */
public interface ITabHostActivity {
	
	public void refresh();
	
	public boolean isActiveTab(Class<?> tabClass);

}
