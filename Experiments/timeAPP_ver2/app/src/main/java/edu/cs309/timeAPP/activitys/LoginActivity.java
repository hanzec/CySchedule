package edu.cs309.timeAPP.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.cs309.timeAPP.R;

public class LoginActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	private void initView () {
		initNavBar(false, "Login", false);
	}
}
