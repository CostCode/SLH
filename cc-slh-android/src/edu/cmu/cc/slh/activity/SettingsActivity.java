/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import java.util.List;

import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.SettingsAdapter;
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
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

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
	public void init(ITabHostActivity tabHost) {}
	
	@Override
	public boolean prepareOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean handleOptionsMenuItemSelection(MenuItem item) {
		return false;
	}

	@Override
	public void refresh() {}

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
		
		prepareSelectedWarehouse(warehouses);
	}

	private void prepareSelectedWarehouse(List<Warehouse> warehouses) {
		
		long warehouseId = SettingsAdapter.retrieveSelectedWarehouseId();
		
		if (warehouseId <= 0) {
			Warehouse selectedWarehouse = warehouses.get(0);
			SettingsAdapter.persistSelectedWarehouseId(
					selectedWarehouse.getId());
		}
	}
	
}
