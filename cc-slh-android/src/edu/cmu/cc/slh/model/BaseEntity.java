/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;

/**
 *  DESCRIPTION: Base entity class for all entity classes.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 4, 2013
 */
public abstract class BaseEntity {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	
	/** Entity id */
	protected long id;

	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

}
