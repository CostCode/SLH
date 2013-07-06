/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;

import java.math.BigDecimal;


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
	
	/** Product category id of this item */
	private long categoryId;
	
	/** Name of the shopping list item */
	private String name;
	
	/** Quantity of this item */
	private int quantity;
	
	/** Price of the item */
	private BigDecimal price;
	
	/** Unit id */
	private int unit;
	
	/** Description of this item */
	private String description;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	
	public ShoppingListItem() {}

	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------


	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(", ");
		builder.append(categoryId);
		builder.append(", ");
		builder.append(name);
		builder.append(", ");
		builder.append(quantity);
		builder.append(", ");
		builder.append(price);
		
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		ShoppingListItem item = (ShoppingListItem) obj;
		return (id == item.getId());
	}

	@Override
	public int hashCode() {
		return (int)id;
	}
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static class Unit {
		
		public static final int CODE_PC = 1;
		public static final int CODE_LB = 2;
		public static final int CODE_GAL = 3;
		
		private static final String STR_PC = "pc.";
		private static final String STR_LB = "lb.";
		private static final String STR_GAL = "gal.";
		
		/**
		 * Returns short string name of the unit measure by unit code
		 * @param unitCode - code of the unit measure
		 * @return short name of the unit measure
		 * @throws IllegalArgumentException - if the given unit code if not valid
		 */
		public static String getUnitByCode(final int unitCode) 
				throws IllegalArgumentException {
			
			if (unitCode < CODE_PC || unitCode > CODE_GAL) {
				throw new IllegalArgumentException("Invalid Unit code");
			}
			
			switch (unitCode) {
			case CODE_PC:
				return STR_PC;
			case CODE_LB:
				return STR_LB;
			case CODE_GAL:
				return STR_GAL;
			}
			
			return null;
		}
		
	}
	
}
