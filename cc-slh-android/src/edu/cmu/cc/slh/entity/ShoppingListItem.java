/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.entity;


/**
 *  DESCRIPTION: This class specifies a product item in the shopping list.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: May 31, 2013
 */
public class ShoppingListItem extends BaseEntity {

	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	
	/** Shopping list to which this item belongs */
	private ShoppingList shoppingList;
	
	/** Product category of this item */
	private ItemCategory category;
	
	/** Amount this item */
	private int amount;
	
	/** Description of this item */
	private String description;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	
	public ShoppingListItem() {}

	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------


	public ShoppingList getShoppingList() {
		return shoppingList;
	}


	public void setShoppingList(ShoppingList shoppingList) {
		this.shoppingList = shoppingList;
	}


	public ItemCategory getCategory() {
		return category;
	}


	public void setCategory(ItemCategory category) {
		this.category = category;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
}
