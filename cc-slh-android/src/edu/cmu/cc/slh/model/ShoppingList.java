/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;

import java.util.Date;


/**
 *  DESCRIPTION: ShoppingList class specifies a user's shopping list.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: May 31, 2013
 */
public class ShoppingList extends BaseEntity {

	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	
	/** Owner of the shopping list */
	private User owner;
	
	/** Shopping list name */
	private String name;
	
	/** Shopping list creation date */
	private Date date;
	
	/** Shopping list status: open or closed */
	private boolean status;
	
	/** Description information */
	private String description;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	
	public ShoppingList() {}

	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	

	public User getOwner() {
		return owner;
	}


	public void setOwner(User owner) {
		this.owner = owner;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
}
