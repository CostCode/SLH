/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.view.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.cmu.cc.slh.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 *  DESCRIPTION: This adapter provides representation for the active 
 *  shopping list. The SL items are displayed within the categories to which
 *  they belong.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 4, 2013
 */
public class ActiveSLViewAdapter extends BaseAdapter {


	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final int TYPE_CATEGORY = 0;

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private final ArrayAdapter<String> categoryHeaders;
	
	private final Map<String, Adapter> categories;

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ActiveSLViewAdapter(Context ctx, Adapter adapter) {
		categoryHeaders = 
				new ArrayAdapter<String>(ctx, R.layout.active_sl_group);
		categories = new LinkedHashMap<String, Adapter>();
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Adding shopping list item categories into the list
	 * @param categoryName - category name
	 * @param categoryAdapter - items of the category
	 */
	public void addCategory(final String categoryName, 
			Adapter categoryAdapter) {
		
		categoryHeaders.add(categoryName);
		categories.put(categoryName, categoryAdapter);
	}
	

	@Override
	public int getCount() {
		
		int total = 0;
		
		for (Adapter adapter : categories.values()) {
			total += adapter.getCount() + 1;
		}
		
		return total;
	}

	@Override
	public Object getItem(int position) {
		
		for (String categoryName : categories.keySet()) {
			Adapter adapter = categories.get(categoryName);
			int size = adapter.getCount() + 1;
			
			// Check whether the position is within the range of this section
			if (position == 0) {
				return categoryName;
			}
			
			if (position < size) {
				return adapter.getItem(position);
			}
			
			// Move on to the next section
			position -= size;
		}
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		int categoryNumber = 0;
		
		for (String categoryName : categories.keySet()) {
			Adapter adapter = categories.get(categoryName);
			int size = adapter.getCount() + 1;
			
			// Check whether the position is within the range of this section
			if (position == 0) {
				return categoryHeaders
						.getView(categoryNumber, convertView, parent);
			}
			
			if (position < size) {
				return adapter.getView(position-1, convertView, parent);
			}
			
			// Move on to the next section
			position -= size;
			categoryNumber++;
		}
		
		return null;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_CATEGORY);
	}

	@Override
	public int getItemViewType(int position) {
		
		int type = 1;
		
		for (String categoryName : categories.keySet()) {
			Adapter adapter = categories.get(categoryName);
			int size = adapter.getCount() + 1;
			
			// Check whether the position is within the range of this section
			if (position == 0) {
				return TYPE_CATEGORY;
			}
			
			if (position < size) {
				return type + adapter.getItemViewType(position-1);
			}
			
			// Move on to the next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		
		return -1;
	}
	
	
	
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

}
