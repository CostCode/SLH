package edu.cmu.cc.slh.activity;

import edu.cmu.cc.slh.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		TextView tvRegister = (TextView) findViewById(R.id.link_to_register);
		
		tvRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent iRegister = new Intent(getApplicationContext(), 
						RegisterActivity.class);
				startActivity(iRegister);
			}
			
		});
		
	}

}
