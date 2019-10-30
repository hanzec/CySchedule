package com.cs309.cychedule.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager
{
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String TOKEN = "token";
    public static final String TOKEN_ID = "tokenID";
    public static final String REFRESH_KEY = "refreshKey";


    public SessionManager(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String token, String tokenID, String refreshKey)
    {
        editor.putBoolean(LOGIN, true);
        editor.putString(TOKEN, token);
        editor.putString(TOKEN_ID, tokenID);
        editor.putString(REFRESH_KEY, refreshKey);
        editor.apply();
    }

    public boolean isLogin()
    {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin()
    {
        if (!this.isLogin())
        {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((Main3Activity) context).finish();
        }
    }

    public HashMap<String, String> getLoginToken()
    {
        HashMap<String, String> user = new HashMap<>();
        user.put(TOKEN, sharedPreferences.getString(TOKEN, null));
        user.put(TOKEN_ID, sharedPreferences.getString(TOKEN_ID, null));
        user.put(REFRESH_KEY, sharedPreferences.getString(REFRESH_KEY, null));
        return user;
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((Main3Activity) context).finish();
    }
}
