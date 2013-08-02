/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.activity;

import edu.cmu.cc.android.activity.async.AbstractAsyncActivity;
import edu.cmu.cc.android.util.DeviceUtils;
import edu.cmu.cc.android.util.Logger;
import edu.cmu.cc.android.util.StringUtils;
import edu.cmu.cc.android.util.WidgetUtils;
import edu.cmu.cc.slh.ApplicationState;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.adapter.ActivationAdapter;
import edu.cmu.cc.slh.task.ActivationTask;
import edu.cmu.cc.slh.task.ActivationTask.IActivationTaskCaller;
import edu.cmu.cc.slh.view.adapter.ActivationViewAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.Toast;

/**
 *  Activation activity provides UI where a user can enter its Costco
 *  membership id and activate the SLH application.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jun 12, 2013
 */
public class ActivationActivity extends AbstractAsyncActivity 
implements IActivationTaskCaller {

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private ActivationViewAdapter activationViewAdapter;
	
	private View activationView;
	
	//-------------------------------------------------------------------------
	// ACTIVITY METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (ActivationAdapter.retrieveActivationStatus()) {
			
			final String memberId = ActivationAdapter.retrieveMemberId();
			if (!StringUtils.isNullOrEmpty(memberId)) {
				ApplicationState.getInstance().setMemberId(memberId);
				showMainActivity();
				this.finish();
			}
		}
		
		activationView = initializeView();
		setContentView(activationView);
		activationViewAdapter = new ActivationViewAdapter(activationView);
		
		initializeButtons();
	}
	
	//-------------------------------------------------------------------------
	// IAsyncActivity METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void onAsyncTaskSucceeded(Class<?> taskClass) {}

	@Override
	public void onAsyncTaskFailed(Class<?> taskClass, final Throwable t) {
		
		final String errorMsg = getAsyncTaskFailedMessage(taskClass, t);
		
		addTaskToUIQueue(new Runnable() {
			
			@Override
			public void run() {
				DialogInterface.OnClickListener dialogListener =
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, 
									int which) {
								dialog.dismiss();
							}
						};
				
				Logger.logErrorAndAlert(ActivationActivity.this, 
						ActivationActivity.class, errorMsg, t, dialogListener);
			}
		});
	}

	//-------------------------------------------------------------------------
	// IActivationTaskCaller METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void onActivationTaskSucceeded(final String memberId, 
			final boolean activated) {
		
		addTaskToUIQueue(new Runnable() {
			
			@Override
			public void run() {
				DialogInterface.OnClickListener dialogListener =
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, 
									int which) {
								
								if (activated) {
									saveMemberId(memberId);
									showMainActivity();
								}
								dialog.dismiss();
							}
						};
				
				int resultMsgResID = (activated) 
						? R.string.activation_success 
						: R.string.activation_unsuccess;
				
				AlertDialog dialog = WidgetUtils.createOkAlertDialog(
						ActivationActivity.this, R.drawable.accept, 
						R.string.activation_result, 
						getString(resultMsgResID), dialogListener);
				
				dialog.show();
			}
		});
		
		ActivationAdapter.persistActivationStatus(activated);
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	/**
	 * Initializes and returns the activation view
	 * 
	 * @return - activation view
	 */
	private View initializeView() {
		
		LayoutInflater inflater = (LayoutInflater)
				 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 
		View view = inflater.inflate(R.layout.activation, null);
		
		return view;
	}
	
	private String getAsyncTaskFailedMessage(Class<?> taskClass, Throwable t) {
		
		int msgResID = R.string.error_unspecified;
		
		if (taskClass == ActivationTask.class) {
			msgResID = R.string.activation_error_activatonFailed;
		} else {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException("Unexpected class: " 
							+ taskClass.toString()));
		}
		
		return StringUtils.getLimitedString(
				getString(msgResID, t.getMessage()), 200, "...");
	}
	
	private void initializeButtons() {
		
		Button btnActivate = 
				(Button) findViewById(R.id.btnActivationActivate);
		
		btnActivate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				activationViewAdapter.validateAllViews();
				
				if (!activationViewAdapter.areAllViewsValid()) {
					Toast.makeText(ActivationActivity.this, 
							R.string.activation_invalidMembershipField, 
							Toast.LENGTH_LONG).show();
				} else {
					
					String membershipID = ActivationViewAdapter
							.getMembershipID(activationView);
					
					boolean networkConnected = DeviceUtils
							.isNetworkConnectedElseAlert(ActivationActivity.this, 
									ActivationActivity.this.getClass());
					
					if (networkConnected) {
						new ActivationTask(ActivationActivity.this, 
								ActivationActivity.this)
								.execute(membershipID);
					}
				}
			}
		});
	}
	
	private void saveMemberId(final String memberId) {
		
		if (!StringUtils.isNullOrEmpty(memberId)) {
			ApplicationState.getInstance().setMemberId(memberId);
			ActivationAdapter.persistMemberId(memberId);
		}
	}
	
	private void showMainActivity() {
		Intent intent = new Intent(this, SLHTabLayouActivity.class);
		startActivity(intent);
	}

}