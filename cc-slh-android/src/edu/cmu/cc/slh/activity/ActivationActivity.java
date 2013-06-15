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
import edu.cmu.cc.slh.task.ActivationTask;
import edu.cmu.cc.slh.task.ActivationTask.IActivationTaskCaller;
import edu.cmu.cc.slh.view.adapter.ActivationViewAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.Toast;

/**
 *  DESCRIPTION: 
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
	
	private Handler asyncTaskHandler;
	
	private ActivationViewAdapter activationViewAdapter;
	
	private View activationView;
	
	private ApplicationState applicationState;
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PROTECTED METHODS
	//-------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		applicationState = (ApplicationState) getApplication();
		
		activationView = initializeView();
		setContentView(activationView);
		
		activationViewAdapter = new ActivationViewAdapter(activationView);
		
		initializeButtons();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		asyncTaskHandler = new Handler();
	}
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	@Override
	public void onAsyncTaskFailed(Class<?> taskClass, final Throwable t) {
		
		final String errorMsg = getAsyncTaskFailedMessage(taskClass, t);
		
		Runnable callback = new Runnable() {
			
			@Override
			public void run() {
				
				DialogInterface.OnClickListener dialogListener =
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ActivationActivity.this.finish();
							}
						};
				
				Logger.logErrorAndAlert(ActivationActivity.this, 
						ActivationActivity.class, errorMsg, t, dialogListener);
			}
		};
		
		Message osMessage = Message.obtain(this.asyncTaskHandler, callback);
		osMessage.sendToTarget();
	}

	@Override
	public void onActivationTaskSucceeded(final boolean activated) {
		
		Runnable callback = new Runnable() {
			
			@Override
			public void run() {
				
				int resultMsgResID = (activated) 
						? R.string.activation_success 
						: R.string.activation_unsuccess;
				
				AlertDialog dialog = WidgetUtils.createOkAlertDialog(
						ActivationActivity.this, R.drawable.accept, 
						R.string.activation_result, 
						getString(resultMsgResID));
				
				dialog.show();
			}
		};
		
		Message osMessage = Message.obtain(this.asyncTaskHandler, callback);
		osMessage.sendToTarget();
		
		applicationState.setActivated(activated);
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
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
	
	private String getAsyncTaskFailedMessage(Class<?> taskClass, Throwable t) {
		
		int msgResID = R.string.error_unspecified;
		
		if (taskClass == ActivationTask.class) {
			msgResID = R.string.activation_error_activatonFailed;
		} else {
			Logger.logErrorAndThrow(getClass(), 
					new IllegalArgumentException("Unexpected class: " 
							+ taskClass.toString()));
		}
		
		return StringUtils.limitLength(
				getString(msgResID, t.getMessage()), 200, "...");
	}
	
	/**
	 * Initializes and returns the activation view
	 * @return - activation view
	 */
	private View initializeView() {
		
		LayoutInflater inflater = (LayoutInflater)
				 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 
		View view = inflater.inflate(R.layout.activation, null);
		
		return view;
	}
	

}