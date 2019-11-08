package com.cs309.cychedule.utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cs309.cychedule.models.UserToken;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class VolleyRequest {

    /**
     * May failed since volley is request by async method
     */

    Gson gson;

    Context context;

    String result = null;

    boolean success = false;

    public VolleyRequest(Context context){
        gson = new Gson();
        this.context = context;
    }

    public String getResult(){return result;}

    public boolean isSuccess(){return success;}

    public void sendObject(Object object, String url,Context context, int method){
        sendObject(object,url,context,method,new UserToken());
    }

    public void sendObject(final Object object, final String url, Context context, int method, final UserToken userToken){
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(method,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        result = s;
                        success = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                success = false;
                result = volleyError.toString();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", generateToken(url,userToken.getTokenID(),userToken.getSecret(),20000000));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> result = new HashMap<>();
                Field[] allFields = object.getClass().getDeclaredFields();
                for (Field field : allFields) {
                    Class<?> targetType = field.getType();
                    Object objectValue = null;
                    try {
                        objectValue = targetType.newInstance();
                        Object value = field.get(objectValue);
                        result.put(field.getName(), Objects.requireNonNull(value).toString());
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String generateToken(String requestUrl, String tokenID, String password, Integer expireTime){
        Key key = Keys.hmacShaKeyFor(password.getBytes());
        return Jwts.builder()
                .signWith(key)
                .setId(tokenID)
                .setIssuer("CySchedule")
                .claim("requestUrl",requestUrl)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
    }
}
