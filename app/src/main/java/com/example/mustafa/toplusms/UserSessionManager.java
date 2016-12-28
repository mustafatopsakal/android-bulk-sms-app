package com.example.mustafa.toplusms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by mustafa on 8.08.2016.
 */
public class UserSessionManager {
    SharedPreferences pref; // Shared Preferences reference
    SharedPreferences.Editor editor; // Editor reference for Shared preferences

    Context context;
    int PRIVATE_MODE = 0;  // Shared pref mode
    private static  String PREFER_NAME = "AndroidExamplePref";   // Sharedpref file name
    private static  String IS_USER_LOGIN = "IsUserLoggedIn"; // All Shared Preferences Keys
    public static  String KEY_EMAIL = "email"; // Email address (make variable public to access from outside)
    public static  String KEY_PASSWORD = "password"; // User name (make variable public to access from outside)

    public String getUserName()
    {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getPassword()
    {
        return pref.getString(KEY_PASSWORD, null);
    }

    public UserSessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String email, String psw, Boolean keepSession){
        editor.putBoolean(IS_USER_LOGIN, keepSession);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, psw);
        editor.commit();
    }

    public boolean checkLogin(){
        if(this.isUserLoggedIn()){
            return true;
        }
        return false;
    }

    public void logoutUser(){
        editor.putBoolean(IS_USER_LOGIN, false);
        editor.commit();
    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

}
