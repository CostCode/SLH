/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import java.util.List;

import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.Warehouse;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 *  DESCRIPTION: 
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 29, 2013
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity 
implements ITabActivity {

	//-------------------------------------------------------------------------
	// PROTECTED METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.layout.settings);
		setupSettings();
	}
	
	//-------------------------------------------------------------------------
	// ITabActivity METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public boolean prepareOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean handleOptionsMenuItemSelection(MenuItem item) {
		return false;
	}

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private void setupSettings() {
		setupSettingWarehouse();
	}

	//-------------------------------------------------------------------------
	// SETTING: WAREHOUSE
	//-------------------------------------------------------------------------
	
	private void setupSettingWarehouse() {
		
		List<Warehouse> warehouses = 
				ApplicationState.getInstance().getWarehouses();
		
		if (warehouses != null && warehouses.size() > 0) {
			
			ListPreference lpWarehouse = (ListPreference) 
					findPreference(getString(R.string.setting_warehouse_key));
			
			String[] entries = new String[warehouses.size()];
			String[] entryValues = new String[warehouses.size()];
			
			for (int i = 0; i < warehouses.size(); i++) {
				entries[i] = warehouses.get(i).getAddress();
				entryValues[i] = String.valueOf(warehouses.get(i).getId());
			}
			
			lpWarehouse.setEntries(entries);
			lpWarehouse.setEntryValues(entryValues);
		}
	}
	
}
