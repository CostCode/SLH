/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dialog;

import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.view.adapter.ShoppingListViewAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 *  DESCRIPTION: Shopping list detail dialog. This dialog enables a user
 *  to edit an existing shopping list or save a new one.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 14, 2013
 */
public class ShoppingListDialog extends DialogFragment {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private ShoppingListViewAdapter viewAdaptor;
	
	private IShoppingListDialogCaller caller;
	
	private View view;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	private void setCaller(IShoppingListDialogCaller caller) {
		this.caller = caller;
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static ShoppingListDialog newInstance(
			IShoppingListDialogCaller caller) {
		
		ShoppingListDialog dialog = new ShoppingListDialog();
		dialog.setCaller(caller);
		
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialogBuilder = 
				new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = (LayoutInflater) 
				getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = inflater.inflate(R.layout.allshoppinglists_detail, null);
		
		ShoppingListViewAdapter.updateView(view);
		
		viewAdaptor = new ShoppingListViewAdapter(view);
		
		dialogBuilder.setView(view);
		
		dialogBuilder.setPositiveButton(R.string.shoppinglist_save, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Logger.logDebug(ShoppingListDialog.class, "POSITIVE BUTTON!!!!");
			}
		});
		
		dialogBuilder.setNegativeButton(R.string.shoppinglist_cancel, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Logger.logDebug(ShoppingListDialog.class, "NEGATIVE BUTTON!!!!");
			}
		});
		
		
		final AlertDialog dialog = dialogBuilder.create();
		
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(final DialogInterface dlg) {
				
				Logger.logDebug(ShoppingListDialog.class, "onShow WAS CALLED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				
				if (dialog != null) {
					
					// Positive button
					
					Button btnPositive = dialog.getButton(Dialog.BUTTON_POSITIVE);
					btnPositive.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							viewAdaptor.validateAllViews();
							view.invalidate();
							
							if (!viewAdaptor.areAllViewsValid()) {
								
								Logger.logDebug(ShoppingListDialog.class, "Fields are not valid!!!");
								
								Toast.makeText(getActivity(), 
										R.string.shoppinglist_save_invalidFields, 
										Toast.LENGTH_LONG).show();
							} else {
								Logger.logDebug(ShoppingListDialog.class, "Fields are valid!!!");
								
								ShoppingListViewAdapter.updateModel(view);
								caller.onShoppingListUpdated();
								
								Toast.makeText(getActivity(), 
										R.string.shoppinglist_save_success, 
										Toast.LENGTH_LONG).show();
								
								dlg.dismiss();
							}
						}
					});
					
					// Negative button
					Button btnNegative = dialog.getButton(Dialog.BUTTON_NEGATIVE);
					btnNegative.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Logger.logDebug(ShoppingListDialog.class, "CANCEL BUTTON WAS PRESSED!!!!!!");
							
							ApplicationState.getInstance().setShoppingList(null);
							dlg.dismiss();
						}
					});
				}
				
			}
		});
		
		return dialog;
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------

	public interface IShoppingListDialogCaller {
		
		public void onShoppingListUpdated();
		
	}

}
