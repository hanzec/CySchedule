package com.cs309.cychedule.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cs309.cychedule.R;
import com.cs309.cychedule.patterns.Singleton;
import com.cs309.cychedule.services.SocketService;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.ServerResponse;
import com.google.gson.Gson;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * MainActivity is the activity of our app's home page
 * It contains all our functions' fragments
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private static String URL_INFO = "https://dev.hanzec.com/api/v1/user/";
    SessionManager sessionManager;
    private AppBarConfiguration mAppBarConfiguration;
    private String output_dest;
    private String output_text;
    static String userName;
    static String email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        getBaseContext();
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                startActivity(alarm);
            }
        });
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_calendar, R.id.nav_daysCounter,
                R.id.nav_timer, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        startService(new Intent(this, SocketService.class));
        
        Menu menuView = navigationView.getMenu();
        MenuItem _send = menuView.findItem(R.id.nav_send);
        _send.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                outputDialog();
                return true;
            }
        });
        View headerView = navigationView.getHeaderView(0);
        ImageView _avator = headerView.findViewById(R.id.nav_header_avatar);
        final TextView _name = headerView.findViewById(R.id.nav_header_name);
        final TextView _email = headerView.findViewById(R.id.nav_header_email);
        
        final RequestQueue requestQueue = Singleton.getInstance(this).getRequestQueue();
        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            Log.e("TAG",response);
                            Gson gson = new Gson();
                            ServerResponse serverResponse = gson.fromJson(response, ServerResponse.class);
                            Map sr = serverResponse.getResponseBody();
                            if (serverResponse.isSuccess())
                            {
                                userName = (String) sr.get("username");
                                email = (String) sr.get("email");
                                _name.setText(userName);
                                _email.setText(email);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.e("TAG", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.toString());                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> requestHeader = new HashMap<String, String>();
                if(sessionManager.getLoginToken().get("tokenID") != null)
                    requestHeader.put("Authorization", generateToken(
                            "I don't know",
                            sessionManager.getLoginToken().get("tokenID"),
                            sessionManager.getLoginToken().get("secret")));
                return requestHeader;
            }
        };
        //requestQueue.add(stringRequest);
        Singleton.getInstance(this).addToRequestQueue(stringRequest);
//        _name.setText(userName);
//        _email.setText(email);
        _avator.setImageResource(R.drawable.gitcat2);
        _avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getBaseContext(), "Updating avator", Toast.LENGTH_LONG).show();
            }
        });
        _name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getBaseContext(), "Hello, "+_name.getText(), Toast.LENGTH_LONG).show();
            }
        });
        _name.setText("aaaaaa");
        _email.setText("bbbbbbbb");
    }
    
    public String generateToken(String requestUrl, String tokenID, String password){
        if (tokenID.isEmpty()){
            throw new NullPointerException();
        }
        Key key = Keys.hmacShaKeyFor(password.getBytes());
        return Jwts.builder()
                .signWith(key)
                .setId(tokenID)
                .setIssuer("CySchedule")
                .claim("requestUrl",requestUrl)
                .setExpiration(new Date(System.currentTimeMillis() + 20000))
                .compact();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);
        return true;
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_send) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "在写了在写了(抱头", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.action_logout) {
            logoutDiag();
        }
        if (id == R.id.action_exit) {
            alertDiag();
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    
    private void toast(String str){
        Toast.makeText( getBaseContext(), str, Toast.LENGTH_LONG).show();
    }
    
    private void outputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setTitle("Send MSG");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        final EditText input_dest = new EditText(this);
        input_dest.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input_dest.setHint("Receiver(Email)");
        layout.addView(input_dest);
        
        final EditText input_text = new EditText(this);
        input_text.setInputType(InputType.TYPE_CLASS_TEXT);
        input_text.setHint("Text message");
        input_text.setHeight(500);
        layout.addView(input_text);
        
        builder.setView(layout);
        
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                output_dest = input_dest.getText().toString();
                output_text = input_text.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void logoutDiag() {
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setCancelable(false);//是否能背后退取消
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平样式
//        dialog.setTitle("Install multiple bugs onto your computer");
//        final StringBuilder msg = new StringBuilder("Loading~");
//        dialog.setMessage(msg.toString());
//        dialog.setMax(100);
//        dialog.setProgress(5);
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                        Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
//                    }
//                });
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        dialog.show();
//
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                for (int j = 0; j <= 100; j++) {
//
//                    if(j>99){
//                        j-=3;
//                    }
//                    if(j<5){
//                        j++;
//                    }
//                    try {
//                        Thread.sleep(30);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    if(j>=100){
//                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                        Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
//                    }
//                    dialog.setProgress(j);
//
//                    if(j>50) {
//                        if (msg.length() >= 15)
//                            msg.deleteCharAt((int) (Math.random()*10));
//                        msg.append((char) (int) (j*Math.random()*1000));
//                        dialog.setMessage(msg.toString());
//                    }
//                }
//            }
//        }).start();
        sessionManager.logout();
    }
    
    private void alertDiag() {
        AlertDialog.Builder dia =new AlertDialog.Builder(this);
        dia.setTitle("FBI WARNING").setIcon(R.drawable.ic_accessible_black_24dp);
        dia.setCancelable(false);
        dia.setPositiveButton("Sure",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        dia.setNegativeButton("NO!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dia.show();
    }
    
    
}

