/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import edu.cmu.cc.slh.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 11, 2013
 */
@SuppressWarnings("deprecation")
public class SLHTabLayouActivity extends TabActivity {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private TabHost tabHost;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slh_tabhost);
		
		tabHost = getTabHost();
		
		//---------------------------------------------------
		// TAB: All shopping lists
		//---------------------------------------------------
		
		TabSpec allSLTab = tabHost.newTabSpec(getString(R.string.tab_sl_all));
		allSLTab.setIndicator(getString(R.string.tab_sl_all), 
				getResources().getDrawable(R.drawable.icon_photos_tab));
		Intent allSLIntent = new Intent(this, AllSLActivity.class);
		allSLTab.setContent(allSLIntent);
		
		//---------------------------------------------------
		// TAB: Active Shopping List
		//---------------------------------------------------
		
		TabSpec activeSLTab = tabHost.newTabSpec(getString(R.string.tab_sl_active));
		activeSLTab.setIndicator(getString(R.string.tab_sl_active), 
				getResources().getDrawable(R.drawable.icon_songs_tab));
		Intent activeSLIntent = new Intent(this, ActiveSLActivity.class);
		activeSLTab.setContent(activeSLIntent);
		
		//---------------------------------------------------
		// TABHOST: Adding tabs into tab host
		//---------------------------------------------------
		
		tabHost.addTab(allSLTab);
		tabHost.addTab(activeSLTab);
		
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				invalidateOptionsMenu();
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		menu.clear();
		
		IOptionsMenuHandler menuHandler = getCurrentMenuHandler();
		
		return menuHandler.prepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		IOptionsMenuHandler menuHandler = getCurrentMenuHandler();
		
		return menuHandler.handleOptionsMenuItemSelection(item);
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private IOptionsMenuHandler getCurrentMenuHandler() {
		return (IOptionsMenuHandler) getLocalActivityManager()
				.getActivity(getTabHost().getCurrentTabTag());
	}

}
