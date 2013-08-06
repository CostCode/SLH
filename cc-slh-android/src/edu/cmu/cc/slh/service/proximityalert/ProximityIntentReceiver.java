/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.service.proximityalert;

import java.util.List;

import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.activity.SLHTabLayouActivity;
import edu.cmu.cc.slh.adapter.ActiveSLAdapter;
import edu.cmu.cc.slh.adapter.SettingsAdapter;
import edu.cmu.cc.slh.dao.SLItemDAO;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.Section;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.model.ShoppingListItem;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;


/**
 *  DESCRIPTION: 
 *	
 *  @author Nohsam Park, Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 26, 2013
 */
public class ProximityIntentReceiver extends BroadcastReceiver {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final int NOTIFICATION_ID = 1000;
	
	public static final String EVENT_PROXIMITY_ALERT = 
			"edu.cmu.cc.slh.PROXIMITY_ALERT";
	
	private static final String ITEM_SEPARATOR = ",";
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private SLItemDAO itemDAO;

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
		
		itemDAO = new SLItemDAO();
		
		Section section = ApplicationState.getInstance().getNearestSection();
		
		ShoppingList activeSL = ActiveSLAdapter.retrieveActiveSL();
		
		final String notificationItems = 
				composeNotificationItems(section, itemDAO.getAll(activeSL));
		
		if (!StringUtils.isNullOrEmpty(notificationItems)) {
			
			final String notificationMsg = ctx.getString(
					R.string.proximityalert_items, notificationItems);
			
			Toast.makeText(ctx, notificationMsg, Toast.LENGTH_LONG).show();
			
			NotificationManager notificationManager = (NotificationManager) 
					ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(NOTIFICATION_ID, 
					createNotification(ctx, ctx.getString(
							R.string.proximityalert_notification_title), 
							notificationMsg));
		}
		
		itemDAO.close();
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
	
	@SuppressWarnings("deprecation")
	private Notification createNotification(Context ctx, 
			String notificaionTitle, String notificationText) {
		
		Notification notification = new Notification();
		
		notification.icon = R.drawable.notification;
		notification.when = System.currentTimeMillis();
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
		
		if (SettingsAdapter.retrieveProximityAlertVibration()) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		notification.ledARGB = Color.WHITE;
		notification.ledOnMS = 1500;
		notification.ledOffMS = 1500;
		
		Intent slhIntent = new Intent(ctx, SLHTabLayouActivity.class);
		PendingIntent intent = PendingIntent.getActivity(ctx, 0, slhIntent, 
				PendingIntent.FLAG_CANCEL_CURRENT);
		
		notification.setLatestEventInfo(ctx, notificaionTitle, 
				notificationText, intent);
		
		return notification;
	}

}
