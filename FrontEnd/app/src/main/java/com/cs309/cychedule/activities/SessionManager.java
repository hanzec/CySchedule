package com.cs309.cychedule.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;
import static android.provider.Settings.NameValueTable.NAME;

/**
 * SessionManager is used to store the token
 * It also has the function to check login status, return the login token and logout
 */
public class SessionManager {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	public Context context;
	private int PRIVATE_MODE = 0;
	private String tokenID;
	private String userName;
	
	private static final String PREF_NAME = "LOGIN";
	private static final String LOGIN = "IS_LOGIN";
	private static final String SECRET = "secret";
	private static final String TOKEN_ID = "tokenID";
	private static final String REFRESH_KEY = "refreshKey";
	private static final String USERNAME = "userName";
	private static final String EMAIL = "email";
	
	public SessionManager(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = sharedPreferences.edit();
	}
	
	public SessionManager(String tokenID, String userName) {
		this.tokenID = tokenID;
		this.userName = userName;
	}
	
	public void initSession(String tokenID, String userName) {
		this.tokenID = tokenID;
		this.userName = userName;
	}
	
	public String getTokenID() {
		return this.tokenID;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void createSession(String secret, String tokenID, String refreshKey) {
		editor.putBoolean(LOGIN, true);
		editor.putString(SECRET, secret);
		editor.putString(TOKEN_ID, tokenID);
		editor.putString(REFRESH_KEY, refreshKey);
		editor.apply();
	}
	
	public void storeInfo(String userName, String email) {
		editor.putString(USERNAME, userName);
		editor.putString(EMAIL, email);
		editor.apply();
	}
	
	public boolean isLogin() {
		return sharedPreferences.getBoolean(LOGIN, false);
	}
	
	public void checkLogin() {
		if (!this.isLogin()) {
			Intent i = new Intent(context, LoginActivity.class);
			context.startActivity(i);
			((MainActivity) context).finish();
		}
	}
	
	public HashMap<String, String> getLoginToken() {
		HashMap<String, String> loginToken = new HashMap<>();
		loginToken.put(SECRET, sharedPreferences.getString(SECRET, null));
		loginToken.put(TOKEN_ID, sharedPreferences.getString(TOKEN_ID, null));
		loginToken.put(REFRESH_KEY, sharedPreferences.getString(REFRESH_KEY, null));
		return loginToken;
	}
	
	public HashMap<String, String> getUserInfo() {
		HashMap<String, String> userInfo = new HashMap<>();
		userInfo.put(USERNAME, sharedPreferences.getString(USERNAME, null));
		userInfo.put(EMAIL, sharedPreferences.getString(EMAIL, null));
		return userInfo;
	}
	
	public void logout() {
		editor.clear();
		editor.commit();
		Intent i = new Intent(context, LoginActivity.class);
		context.startActivity(i);
		((MainActivity) context).finish();
	}
}
