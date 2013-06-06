package edu.cmu.cc.slh.activity;

import edu.cmu.cc.slh.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		TextView tvLogin = (TextView) findViewById(R.id.link_to_login);
		
		tvLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent iLogin = new Intent(getApplicationContext(), 
						LoginActivity.class);
				startActivity(iLogin);
			}
			
		});
	}

}
