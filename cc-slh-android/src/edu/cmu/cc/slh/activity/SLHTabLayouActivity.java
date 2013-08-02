/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActiveSLAdapter;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 11, 2013
 */
@SuppressWarnings("deprecation")
public class SLHTabLayouActivity extends TabActivity 
implements ITabHostActivity {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final String TAB_SL_ALL = "tab_sl_all";
	
	private static final String TAB_SL_ACTIVE = "tab_sl_active";
	
	private static final String TAB_SETTINGS = "tab_settings";

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private TabHost tabHost;

	//-------------------------------------------------------------------------
	// ACTIVITY METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slh_tabhost);
		
		tabHost = getTabHost();
		
		setupTab(TAB_SL_ALL, getString(R.string.tab_sl_all), 
				R.drawable.icon_photos_tab, SLActivity.class);
		
		setupTab(TAB_SL_ACTIVE, getString(R.string.tab_sl_active), 
				R.drawable.icon_songs_tab, SLItemsActivity.class);
		
		setupTab(TAB_SETTINGS, getString(R.string.tab_settings), 
				R.drawable.icon_songs_tab, SettingsActivity.class);
		
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				
				invalidateOptionsMenu();
				
				if (tabId.equals(TAB_SL_ACTIVE)) {
					ApplicationState.getInstance().setCurrentSL(
							ActiveSLAdapter.retrieveActiveSL());
					
					getCurrentTabActivity().refresh();
				}
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		menu.clear();
		
		ITabActivity tabActivity = getCurrentTabActivity();
		
		return tabActivity.prepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		ITabActivity tabActivity = getCurrentTabActivity();
		
		return tabActivity.handleOptionsMenuItemSelection(item);
	}

	//-------------------------------------------------------------------------
	// ITabActivity METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void refresh() {
		tabHost.getTabWidget().getChildTabViewAt(1)
			.setEnabled(hasActiveSL());
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void setupTab(final String tag, final String label, 
			int iconResId, Class<?> activity) {
		
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		
		//---------------------------------------------------
		// TAB: Setting up tab indicator
		//---------------------------------------------------
		
		tabSpec.setIndicator(createTabIndicator(label, iconResId));
		
		//---------------------------------------------------
		// TAB: Setting up tab activity
		//---------------------------------------------------
		
		Intent tabIntent = new Intent(this, activity);
		tabSpec.setContent(tabIntent);
		
		//---------------------------------------------------
		// TAB: Adding to the tab host
		//---------------------------------------------------
		
		tabHost.addTab(tabSpec);
	}
	
	private View createTabIndicator(final String label, int iconResId) {
		
		View tabIndicator = 
				getLayoutInflater().inflate(R.layout.tab_indicator, null);
		
		ImageView ivIcon = (ImageView) 
				tabIndicator.findViewById(R.id.iv_tab_indicator_icon);
		ivIcon.setImageResource(iconResId);
		
		TextView tvTitle = (TextView) 
				tabIndicator.findViewById(R.id.tv_tab_indicator_title);
		tvTitle.setText(label);
		
		return tabIndicator;
	}
	
	private ITabActivity getCurrentTabActivity() {
		return (ITabActivity) getLocalActivityManager()
				.getActivity(getTabHost().getCurrentTabTag());
	}
	
	private boolean hasActiveSL() {
		return (ActiveSLAdapter.retrieveActiveSL() != null);
	}

}
