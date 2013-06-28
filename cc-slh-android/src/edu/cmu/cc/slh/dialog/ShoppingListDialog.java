/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.dialog;

import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.model.ShoppingList;
import edu.cmu.cc.slh.view.adapter.ShoppingListViewAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
	
	private Context ctx;
	
	private ShoppingListViewAdapter viewAdaptor;
	
	private IShoppingListDialogCaller caller;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	private void setContext(Context ctx) {
		this.ctx = ctx;
	}
	
	private void setCaller(IShoppingListDialogCaller caller) {
		this.caller = caller;
	}

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static ShoppingListDialog newInstance(Context ctx, 
			IShoppingListDialogCaller caller) {
		
		ShoppingListDialog dialog = new ShoppingListDialog();
		dialog.setContext(ctx);
		dialog.setCaller(caller);
		
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
		
		LayoutInflater inflater = (LayoutInflater) 
				ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final View view = inflater.inflate(R.layout.allshoppinglists_detail, null);
		
		viewAdaptor = new ShoppingListViewAdapter(view);
		
		dialogBuilder.setView(view);
		
		dialogBuilder.setPositiveButton(R.string.shoppinglist_save, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				viewAdaptor.validateAllViews();
				
				if (!viewAdaptor.areAllViewsValid()) {
					Toast.makeText(ctx, 
							R.string.shoppinglist_save_invalidFields, 
							Toast.LENGTH_LONG).show();
				} else {
					ShoppingList sl = ShoppingListViewAdapter.fromView(view);
					ApplicationState.getInstance().setShoppingList(sl);
					caller.onShoppingListSaved();
					
					dialog.dismiss();
					
					Toast.makeText(ctx, 
							R.string.shoppinglist_save_success, 
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		dialogBuilder.setNegativeButton(R.string.shoppinglist_cancel, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				ApplicationState.getInstance().setShoppingList(null);
				dialog.cancel();
			}
		});
		
		return dialogBuilder.create();
	}
	
	

	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	public interface IShoppingListDialogCaller {
		
		public void onShoppingListSaved();
		
	}

}
