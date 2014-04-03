package ctl.dev.extremescanner2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {

	Button btn_login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		btn_login = (Button) findViewById(R.id.login_btn_login);
		
		btn_login.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, BrowseActivity.class));
				finish();
			}
		});
	}

}
