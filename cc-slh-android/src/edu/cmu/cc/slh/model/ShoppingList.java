/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.cmu.cc.android.util.StringUtils;


/**
 *  DESCRIPTION: ShoppingList class specifies a user's shopping list.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: May 31, 2013
 */
public class ShoppingList extends BaseEntity {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	
	/** Shopping list name */
	private String name;
	
	/** Shopping list creation date */
	private Date date;
	
	/** Description information */
	private String description;
	
	/** Version number */
	private int version;
	
	/** Items of this shopping list */
	private List<ShoppingListItem> items;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	
	public ShoppingList() {}
	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	

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


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public List<ShoppingListItem> getItems() {
		return items;
	}
	public void setItems(List<ShoppingListItem> items) {
		this.items = items;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	public void addItem(ShoppingListItem item) {
		if (items == null) {
			items = new ArrayList<ShoppingListItem>();
		}
		items.add(item);
	}
	

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(", ");
		builder.append(name);
		builder.append(", ");
		builder.append(StringUtils.getDateAsString(date, DATE_PATTERN));
		builder.append(", ");
		builder.append(version);
		
		return builder.toString();
	}
	
}
