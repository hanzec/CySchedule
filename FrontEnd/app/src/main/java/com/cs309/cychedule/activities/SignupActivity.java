package com.cs309.cychedule.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.cs309.cychedule.R;
import com.cs309.cychedule.utilities.UserUtil;
import com.cs309.cychedule.patterns.Singleton;

public class SignupActivity extends AppCompatActivity {

    private static String URL_SIGNUP = "https://dev.hanzec.com/api/v1/auth/register";
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_userName) EditText _userNameText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_firstName) EditText _firstNameText;
    @BindView(R.id.input_lastName) EditText _lastNameText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() throws JSONException {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String userName = _userNameText.getText().toString().trim();
        final String password = _passwordText.getText().toString().trim();
        final String reEnterPassword = _reEnterPasswordText.getText().toString().trim();
        final String email = _emailText.getText().toString().trim();
        final String firstName = _firstNameText.getText().toString().trim();
        final String lastName = _lastNameText.getText().toString().trim();
//
//        // TODO: Implement your own signup logic here.
//
//        Log.d("Inputs:", userName);
//        System.out.println(userName + password + email + firstName + lastName);

        RequestQueue requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();
        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();

//        JSONObject jsonObject = new JSONObject();
//        try
//        {
//            jsonObject.put("userName", userName);
//            jsonObject.put("password", password);
//            jsonObject.put("email", email);
//            jsonObject.put("firstName", firstName);
//            jsonObject.put("lastName", lastName);
//        }
//        catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGNUP, jsonObject,
//                new ServerResponse.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        try
//                        {
//                            String status = response.getString("status");
//                            if (status.equals("201"))
//                            {
//                                Toast.makeText(SignupActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                            Toast.makeText(SignupActivity.this, "Register Error! " + e.toString(), Toast.LENGTH_SHORT).show();
//                            _signupButton.setVisibility(View.VISIBLE);
//                        }
//                    }
//                },
//                new ServerResponse.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Toast.makeText(SignupActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
//                        _signupButton.setVisibility(View.VISIBLE);
//                        Log.d("Error", error.toString());
//                    }
//                }
//        )
//        {
//            @Override
//            public int getMethod() {
//                return Method.POST;
//            }
//
//            @Override
//            public Priority getPriority() {
//                return Priority.NORMAL;
//            }
//        };
//        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        //requestQueue.add(jsonObjectRequest);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if (status.equals("201"))
                            {
                                Toast.makeText(SignupActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                                onSignupSuccess();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            _signupButton.setVisibility(View.VISIBLE);
                            Toast.makeText(SignupActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            onSignupFailed();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        _signupButton.setVisibility(View.VISIBLE);
                        Toast.makeText(SignupActivity.this, "Register Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        onSignupFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", password);
                params.put("email", email);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                return params;
            }
        };
        Singleton.getInstance(this).addToRequestQueue(stringRequest);
        //requestQueue.add(stringRequest);

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userName = _userNameText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if(Patterns.EMAIL_ADDRESS.matcher(userName).matches() || Patterns.PHONE.matcher(userName).matches()){
            _userNameText.setError(null);
        }
        else if (userName.isEmpty()) {
            _userNameText.setError("Please enter your email address or phone number!");
            valid = false;
        }
        else if ((userName.contains("@") ||userName.contains(".com")) && !Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            _userNameText.setError("Please enter a valid email address!");
            valid = false;
        }
        else if ( UserUtil.isNumeric(userName) && !Patterns.PHONE.matcher(userName).matches()) {
            _userNameText.setError("Please enter a valid phone number!");
            valid = false;
        }
        else if ( UserUtil.hasSpecialChar(userName)) {
            _userNameText.setError("Please enter a valid email address or phone number!");
            valid = false;
        }
        else {
            _userNameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
            _passwordText.setError("Between 4 and 12 alphanumeric characters.");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!reEnterPassword.equals(password)) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}