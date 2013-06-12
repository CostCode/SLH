/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.db;

/**
 *  DESCRIPTION: Local DB tables descriptions. 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 4, 2013
 */
public final class DBContract {

	
	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	
	/**
	 * ItemCategory DB table description.
	 */
	public static abstract class ItemCategory {
		
		/** TABLE NAME */
		public static final String TABLE_NAME = "itemcategory";
		
		/** PRIMARY KEY: Product item category id */
		public static final String ID = "id";
		
		/** FOREIGN KEY: Parent item category */
		public static final String CATEGORY = "category_id";
		
		/** COLUMN: Item category name */
		public static final String NAME = "name";
		
		/** COLUMN: Item category description */
		public static final String DESCRIPTION = "description";
		
	}
	
	
	/**
	 * ShoppingList DB table description.
	 */
	public static abstract class ShoppingList {
		
		/** TABLE NAME */
		public static final String TABLE_NAME = "shoppinglist";
		
		/** PRIMARY KEY: Shopping list id */
		public static final String ID = "id";
		
		/** COLUMN: Shopping list name */
		public static final String NAME = "name";
		
		/** COLUMN: Shopping list creation date */
		public static final String DATE = "date";
		
		/** COLUMN: Item category description */
		public static final String DESCRIPTION = "description";
		
	}
	
	
	/**
	 * ShoppingListItem DB table description.
	 */
	public static abstract class ShoppingListItem {
		
		/** TABLE NAME */
		public static final String TABLE_NAME = "shoppinglistitem";
		
		/** PRIMARY KEY: Shopping list item id */
		public static final String ID = "id";
		
		/** FOREIGN KEY: Shopping list of the item */
		public static final String SHOPPING_LIST = "shoppinglist_id";
		
		/** FOREIGN KEY: Shopping list item category */
		public static final String CATEGORY = "itemcategory_id";
		
		/** COLUMN: Shopping list item quantity */
		public static final String QUANTITY = "quantity";
		
		/** COLUMN: Shopping list item units */
		public static final String UNIT = "unit";
		
		/** COLUMN: Item category description */
		public static final String DESCRIPTION = "description";
		
	}


}
