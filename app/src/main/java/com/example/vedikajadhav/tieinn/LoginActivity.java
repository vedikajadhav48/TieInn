package com.example.vedikajadhav.tieinn;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vedikajadhav.tieinnLibrary.CustomAlertDialog;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.example.vedikajadhav.tieinnModel.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity{
    private static final String TAG= "LoginActivity";
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private LoginButton mFacebookLoginButton;
    private String mUsername;
    private String mPassword;
    private String mUserID;
    private String mProfileName;
    private CallbackManager callbackManager;
    SessionManager mSession;
    ProfileTracker profileTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Vedika LoginActivity: onCreate");
        //Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Initialize the CallbackManager using the
        // CallbackManager.Factory.create() method to
        // create a new instance of it.
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        // Session class instance
        mSession = SessionManager.getInstance(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(mSession.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
/*                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
            startActivity(intent);
            finish();
        }

        //Initialize the views
        mUserNameEditText = (EditText)findViewById(R.id.edit_text_username_to_login);
        mPasswordEditText = (EditText)findViewById(R.id.edit_text_password_to_login);
        mFacebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);

        //Initialize the ProfileTracker and override its onCurrentProfileChanged(...) method.
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //Whenever the user profile is changed, this method will be called.
                Log.i(TAG, "oldProfile" + oldProfile);
                Log.i(TAG, "new Profile" + newProfile);
                if (newProfile == null) {
                    Log.i(TAG, "new profile is null");
                    //profileImageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                    //profileInfoTextView.setText("");
                }else{
                    setUpImageAndInfo(newProfile);
                }
            }
        };
        //profileTracker.startTracking();

        //Initialize the FacebookCallback and then override its methods
        // for performing actions.
        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mUsername = loginResult.getAccessToken().getUserId();//UserID
               // mAccessToken = loginResult.getAccessToken().getToken();//AuthToken
                /*Profile userProfile = Profile.getCurrentProfile();
                setUpImageAndInfo(userProfile);*/
                profileTracker.startTracking();
                facebookLogin(getApplicationContext());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "onError");
            }
        });
    }

    public void facebookLogin(final Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FACEBOOK_LOGIN_URL,
                new Response.Listener<String>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObjectResponse = new JSONObject(response);
                            success = jsonObjectResponse.getInt("success");
                            message = jsonObjectResponse.getString("message");

                            if (success == 1) {
                                mUserID = message;
                                mSession = SessionManager.getInstance(context);
                                mSession.createLoginSession(mUserID, mUsername, mProfileName);
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                startActivity(intent);
                                // this finish() method is used to tell android os that we are done with current
                                // activity now! Moving to other activity
                                 finish();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Invalid username/password", message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i(TAG, "Volley  Error"+ error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("profileName", mProfileName);
                params.put("username", mUsername);
                params.put("password", mPassword);
                Log.i(TAG,"params fbLogin" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        queue.add(stringRequest);
    }

    public void login(View button){
        mUsername = mUserNameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        if((mUsername.equals("")) || (mPassword.equals(""))){
            CustomAlertDialog.showAlertDialog(this, "Username and/or Password field empty", "Enter Username and Password");
        }else {
            tieInnLogin(this);
        }
    }

    public void tieInnLogin(final Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOG_URL,
                new Response.Listener<String>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject person = new JSONObject(response);
                            success = person.getInt("success");
                            message = person.getString("message");

                            if (success == 1) {
                                JSONObject userInfoResponse = new JSONObject(message);
                                mUserID = userInfoResponse.getString(Constants.TAG_USERID);
                                mProfileName = userInfoResponse.getString(Constants.TAG_PROFILE_NAME);
                                mSession = SessionManager.getInstance(context);
                                mSession.createLoginSession(mUserID, mUsername, mProfileName);
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                startActivity(intent);
                                // this finish() method is used to tell android os that we are done with current
                                // activity now! Moving to other activity
                                finish();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Invalid username/password", message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mUsername);
                params.put("password", mPassword);
                Log.i(TAG,"params tieInnLogin" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        queue.add(stringRequest);
    }

    public void signUp(View button){
        Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
        startActivity(createAccountIntent);
    }

    public void forgotPassword(View button){
        Log.i(TAG, "Forgot Password");
        Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(forgotPasswordIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG,"Vedika LoginActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Vedika LoginActivity: onResume");
       /* Profile userProfile = Profile.getCurrentProfile();
        if (userProfile != null){
            setUpImageAndInfo(userProfile);
        }else{
            Log.d(TAG,"Profile is Null");
        }*/
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "Vedika LoginActivity: onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Vedika LoginActivity: onStop");
        //profileTracker.stopTracking();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Vedika LoginActivity: onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        Log.i(TAG, "Vedika LoginActivity: onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "Vedika LoginActivity: onBackPressed");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "Vedika LoginActivity: onSaveInstanceState");
    }

    public void setUpImageAndInfo(Profile userProfile) {
        //This method will fill up the ImageView and TextView
        // that we initialized before.
        final String userInfo = "<u>First Name:</u> " + userProfile.getFirstName() +
                "<br/><u>Last Name:</u> " + userProfile.getLastName() +
                "<br/><u>User Id:</u> " + userProfile.getId() +
                "<br/><u>Profile Link:</u> " + userProfile.getLinkUri().toString();
        Log.i(TAG, "userInfo" + userInfo);
        mProfileName = userProfile.getFirstName() + " " + userProfile.getLastName();
        mPassword = userProfile.getFirstName();
        //profileInfoTextView.setText(Html.fromHtml(userInfo));


        //I am using the Picasso library to download the image
        // from URL and then load it to the ImageView.
       /* Picasso.with(this)
                .load("https://graph.facebook.com/" + userProfile.getId().toString() + "/picture?type=large")
                .into(profileImageView);*/
    }
}
