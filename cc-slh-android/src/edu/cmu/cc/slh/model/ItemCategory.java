/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;


/**
 *  DESCRIPTION: This class specifies Costco products category.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: May 31, 2013
 */
public class ItemCategory extends BaseEntity {

	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	
	/** Parent Category of this product category*/
	private ItemCategory category;
	
	/** Name of the product category */
	private String name;
	
	/** Description of this product category */
	private String description;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	
	public ItemCategory() {}


	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	
	public ItemCategory getCategory() {
		return category;
	}


	public void setCategory(ItemCategory category) {
		this.category = category;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
}
