package com.cs309.cychedule.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs309.cychedule.R;

import java.util.HashMap;

import butterknife.BindView;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.input_userName) TextView _userName;
    @BindView(R.id.btn_logout) Button _logoutButton;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        _userName = findViewById(R.id.input_userName);
        _logoutButton = findViewById(R.id.btn_logout);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String theName = user.get(sessionManager.EMAIL);

        _userName.setText(theName);

        _logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                sessionManager.logout();
            }
        });
    }
}
