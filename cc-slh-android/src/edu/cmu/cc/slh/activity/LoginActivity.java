package edu.cmu.cc.slh.activity;

import edu.cmu.cc.slh.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private Button btnLogin;
	private EditText etUsername;
	private EditText etPassword;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO
			}
			
		});
		
		
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