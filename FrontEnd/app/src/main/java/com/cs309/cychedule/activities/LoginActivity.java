package com.cs309.cychedule.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.cs309.cychedule.R;
import com.cs309.cychedule.models.ServerResponse;
import com.cs309.cychedule.models.UserToken;
import com.cs309.cychedule.patterns.Singleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private static String URL_LOGIN = "https://dev.hanzec.com/api/v1/auth/login";
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
//    private static String EMAIL = null;
    
    ProgressDialog progressDialog;
        
        
        
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

//    private void initData() {
//        SharedPreferences sp = getSharedPreferences("loginToken", 0);
//        EMAIL = sp.getString("email", null);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        _loginButton.setEnabled(false);
    progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        
        final RequestQueue requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();
        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
//                            JSONObject object = new JSONObject(response);
//                            String status = object.getString("status");
//                            JSONObject loginToken = object.getJSONObject("responseBody");

                            Gson gson = new Gson();
                            ServerResponse serverResponse = gson.fromJson(response, ServerResponse.class);
                            Map loginToken = (Map) serverResponse.getResponseBody().get("loginToken");
                            if (serverResponse.isSuccess())
                            {
                                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                String secret = (String) loginToken.get("secret");
                                String tokenID = (String) loginToken.get("tokenID");
                                String refreshKey = (String) loginToken.get("refreshKey");
//                                String token = loginToken.getString("token").trim();
//                                String tokenID = loginToken.getString("tokenID").trim();
//                                String refreshKey = loginToken.getString("refreshKey").trim();
                                sessionManager.createSession(secret, tokenID, refreshKey);
                                onLoginSuccess();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Login Failed. Please check your password.", Toast.LENGTH_SHORT).show();
                                onLoginFailed();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            _loginButton.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            onLoginFailed();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _loginButton.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Login Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        onLoginFailed();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        //requestQueue.add(stringRequest);
        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onLoginFailed() {
        progressDialog.cancel();
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

}
