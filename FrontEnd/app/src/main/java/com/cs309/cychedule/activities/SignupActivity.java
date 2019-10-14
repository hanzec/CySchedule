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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.volley.toolbox.Volley;
import com.cs309.cychedule.R;
import com.cs309.cychedule.utilities.userUtil;
import com.cs309.cychedule.patterns.Singleton;

import javax.xml.transform.ErrorListener;

public class SignupActivity extends AppCompatActivity {


   //private static String URL_SIGNUP = "http://10.26.187.17:8080/api/v1/auth/register?username=N22L&password=fuck&email=nononono@fucker.edu&firstname=yello&lastname=mello";
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
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    public void signup() {
        Log.d(TAG, "Signup");

//        if (!validate()) {
//            onSignupFailed();
//            return;
//        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String userName = this._userNameText.getText().toString();
        final String password = this._passwordText.getText().toString();
        final String reEnterPassword = this._reEnterPasswordText.getText().toString();
        final String email = this._emailText.getText().toString();
        final String firstName = this._firstNameText.getText().toString();
        final String lastName = this._lastNameText.getText().toString();
        
    //        _signupButton.setVisibility(View.GONE);

        //final String userName = this._userNameText.toString().trim();
        //final String password = this._passwordText.toString().trim();

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response)
//                    {
//                        try
//                        {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//
//                            if (success.equals("1"))
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
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Toast.makeText(SignupActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
//                        _signupButton.setVisibility(View.VISIBLE);
//                    }
//                }
//
//        )
//
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("userName", userName);
//                params.put("password", password);
//                params.put("email", email);
//                params.put("firstName", firstName);
//                params.put("lastName", lastName);
//                return super.getParams();
//            }
//        };



        /**GET*/
//        StringRequest stringRequest = new StringRequest(URL_SIGNUP,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("TAG", response);
//                        Toast.makeText(SignupActivity.this, response , Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", error.getMessage(), error);
//            }
//        }
//        );


        /**MAP*/
            //
            // RequestQueue requestQueue = Volley.newRequestQueue(this);
            // requestQueue.start();
            // StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
            //         new Response.Listener<String>() {
            //             @Override
            //             public void onResponse(String response) {
            //                 Log.d("TAG", response);
            //                 Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
            //             }
            //         }, new Response.ErrorListener() {
            //     @Override
            //     public void onErrorResponse(VolleyError error) {
            //         Log.e("TAG", error.getMessage(), error);
            //     }
            // }) {
            //     @Override
            //     protected Map<String, String> getParams() throws AuthFailureError {
            //         Map<String, String> map = new HashMap<String, String>();
            //         map.put("userame", "111111111111111");
            //         map.put("password", "WOOHOO");
            //         map.put("email", "FOO");
            //         map.put("firstName", "BAR");
            //         map.put("lastName", "BUGS");
            //         return map;
            //     }
            // };
            //
            // requestQueue.add(stringRequest);

/**V3*/

       RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
rq.start();
       JSONObject params = new JSONObject();
       try {
           params.put("usename", "LAHEE");
           params.put("password", "YAHOO");
           params.put("email", "YAHOO");
           params.put("firstName", "YAHOO");
           params.put("lastName", "YAHOO");
       } catch (JSONException e) {
           e.printStackTrace();
       }

       JsonObjectRequest thisIsSth = new JsonObjectRequest(Request.Method.POST,
               URL_SIGNUP, params, //Not null???
               new Response.Listener<JSONObject>() {

                   @Override
                   public void onResponse(JSONObject response) {
                       Log.d(TAG, response.toString());
                       // pDialog.hide();
                   }
               }, new Response.ErrorListener() {

           @Override
           public void onErrorResponse(VolleyError error) {
               VolleyLog.d(TAG, "WTF? : " + error.getMessage());
               //pDialog.hide();
           }
       });

// Adding request to request queue
       rq.add(thisIsSth);

/**JSOBJ*/
//         try {
//             RequestQueue requestQueue = Volley.newRequestQueue(this);
//             JSONObject jsonBody = new JSONObject();
//             jsonBody.put("NMSL", "NMSSSSSSSSL");
//             jsonBody.put("NMSL", "YAHOO");
//             final String requestBody = jsonBody.toString();
//             //NMSL&password=peach&email=nononono@lemon.edu&firstname=yello&lastname=mello";
//             URL_SIGNUP+=userName;
//             URL_SIGNUP+="&password=";
//             URL_SIGNUP+=password;
//             URL_SIGNUP+="&email=";
//             URL_SIGNUP+=email;
//             URL_SIGNUP+="&firstname=";
//             URL_SIGNUP+=firstName;
//             URL_SIGNUP+="&lastname=";
//             URL_SIGNUP+=lastName;
//             StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
//                     new Response.Listener<String>() {
//                 @Override
//                 public void onResponse(String response) {
//                     Log.i("VOLLEY", response);
//
// //                    if(response.equals("200")){
// //                        Toast.makeText(SignupActivity.this, "Sign Up Successfully!" , Toast.LENGTH_SHORT).show();
// //                        response=null;
// //                    }
// //                    else{
//                         Toast.makeText(SignupActivity.this, "Server MSG: "+response , Toast.LENGTH_LONG).show();
//                         response=null;
// //                    }
//                 }
//             }, new Response.ErrorListener() {
//                 @Override
//                 public void onErrorResponse(VolleyError error) {
//                     Log.e("VOLLEY", error.toString());
//                 }
//             }) {
//                 @Override
//                 protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                     String responseString = "";
//                     if (response != null) {
//                         responseString = String.valueOf(response.statusCode);
//                         // can get more details such as response.headers
//                     }
//                     return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                 }
//             };
//
//             requestQueue.add(stringRequest);
//         } catch (JSONException e) {
//             e.printStackTrace();
//         }

//        RequestQueue requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();
//        Singleton.getInstance(this).addToRequestQueue(stringRequest);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                         onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Please Check Network!", Toast.LENGTH_LONG).show();
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
        else if ( userUtil.isNumeric(userName) && !Patterns.PHONE.matcher(userName).matches()) {
            _userNameText.setError("Please enter a valid phone number!");
            valid = false;
        }
        else if ( userUtil.hasSpecialChar(userName)) {
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