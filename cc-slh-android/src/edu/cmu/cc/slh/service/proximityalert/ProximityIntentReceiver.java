/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.service.proximityalert;

import java.util.List;

import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.Section;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 26, 2013
 */
public class ProximityIntentReceiver extends BroadcastReceiver {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	public static final String EVENT_PROXIMITY_ALERT = 
			"edu.cmu.cc.slh.PROXIMITY_ALERT";
	
	private static final String ITEM_SEPARATOR = ",";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		Section section = ApplicationState.getInstance().getNearestSection();
		
		ShoppingList currentSL = 
				ApplicationState.getInstance().getCurrentSL();
		
		final String notificationItems = 
				composeNotificationItems(section, currentSL.getItems());
		
		if (!StringUtils.isNullOrEmpty(notificationItems)) {
			Toast.makeText(ctx, ctx.getString(
					R.string.proximityalert_items, notificationItems), 
					Toast.LENGTH_LONG).show();
		}
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private String composeNotificationItems(Section section, 
			List<ShoppingListItem> items) {
		
		if (section == null || section.getCategories() == null 
				|| items == null || items.size() == 0) {
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		
		for (ShoppingListItem item : items) {
			if (isItemToBeNotified(item, section.getCategories())) {
				builder.append(item.getName());
				builder.append(ITEM_SEPARATOR);
			}
		}
		
		return builder.toString();
	}
	
	private boolean isItemToBeNotified(ShoppingListItem item, 
			List<ItemCategory> categories) {
		
		for (ItemCategory category : categories) {
			if (category.equals(item.getCategory())) {
				return true;
			}
		}
		
		return false;
	}

}
