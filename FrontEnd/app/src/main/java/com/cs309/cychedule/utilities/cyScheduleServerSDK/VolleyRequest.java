package com.cs309.cychedule.utilities.cyScheduleServerSDK;

import android.content.Context;
import android.os.RemoteException;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.ServerResponse;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.UserToken;
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

    private Context context;

    private Integer expireTime = 0;



    public VolleyRequest(Context context){
        this(context,200000);
    }

    public VolleyRequest(Context context,Integer expireTime){
        this.context = context;
        this.expireTime = expireTime;
    }
    public void sendObject(final Object object, String url, int method,ResponseHandler responseHandler){
        sendObject(object,url,method,new UserToken(),responseHandler);
    }

    public void sendObject(final Object object, final String url, int method, final UserToken userToken, final ResponseHandler responseHandler){
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(method,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                      responseHandler.handle(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                responseHandler.faildHandler(volleyError);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                if(!userToken.getTokenID().isEmpty())
                    params.put("Authorization", generateToken(url,userToken.getTokenID(),userToken.getSecret()));
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

    private String generateToken(String requestUrl, String tokenID, String password){
        Key key = Keys.hmacShaKeyFor(password.getBytes());
        return Jwts.builder()
                .signWith(key)
                .setId(tokenID)
                .setIssuer("CySchedule")
                .claim("requestUrl",requestUrl)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
    }

    public abstract static class ResponseHandler {
        private Gson gson = new Gson();
        abstract void faildHandler(VolleyError volleyError);
        abstract void succeessHandler(ServerResponse serverResponse);

        public void handle(String s){
            succeessHandler(gson.fromJson(s,ServerResponse.class));
        }
    }
}
