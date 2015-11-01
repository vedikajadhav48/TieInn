package com.aztecFAQ.vedikajadhav.tieinnLibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.aztecFAQ.vedikajadhav.tieinn.LoginActivity;
import java.util.HashMap;

/**
 * Created by Vedika Jadhav on 9/10/2015.
 */
public class SessionManager{
    private static final String TAG= "SessionManager";
    private static SessionManager mSessionInstance;

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared pref file name
    private static final String PREF_NAME = "TieinnPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // UserID (make variable public to access from outside)
    public static final String KEY_USERID = "userID";

    // User name (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";

    // Profile name (make variable public to access from outside)
    public static final String KEY_PROFILE_NAME = "profileName";

    // Constructor
    private SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //static instance method
    public static synchronized SessionManager getInstance(Context context){
        if(mSessionInstance == null){
            mSessionInstance = new SessionManager(context);
        }
        return mSessionInstance;
    }


    // Create login session
    public void createLoginSession(String userID, String username, String profileName){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //Storing UserID in pref
        editor.putString(KEY_USERID, userID);

        // Storing name in pref
        editor.putString(KEY_USERNAME, username);

        // Storing email in pref
        editor.putString(KEY_PROFILE_NAME, profileName);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    //Get stored session data
    public HashMap<String, String > getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        // user id
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        // user name
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        // profile name
        user.put(KEY_PROFILE_NAME, pref.getString(KEY_PROFILE_NAME, null));

        // return user
        return user;
    }


    // Clear session details
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Starting Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
