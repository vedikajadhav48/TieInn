package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputService;
import android.service.textservice.SpellCheckerService;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vedikajadhav.tieinnLibrary.AppController;
import com.example.vedikajadhav.tieinnLibrary.CustomAlertDialog;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.example.vedikajadhav.tieinnModel.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

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
    private String mFacebookUserID;
    private String mAccessToken;
    private CallbackManager callbackManager;
    SessionManager mSession;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Initialize the CallbackManager using the
        // CallbackManager.Factory.create() method to
        // create a new instance of it.
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        //Initialize the views
        mUserNameEditText = (EditText)findViewById(R.id.edit_text_username_to_login);
        mPasswordEditText = (EditText)findViewById(R.id.edit_text_password_to_login);
        mFacebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);

        //Initialize the ProfileTracker and override its
        // onCurrentProfileChanged(...) method.
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //Whenever the user profile is changed,
                //this method will be called.
                if (newProfile == null) {
                    //profileImageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                    //profileInfoTextView.setText("");
                }else{
                    setUpImageAndInfo(newProfile);
                }
            }
        };
        profileTracker.startTracking();

        //Initialize the FacebookCallback and then override its methods
        // for performing actions.
        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mUsername = loginResult.getAccessToken().getUserId();//UserID
                mAccessToken = loginResult.getAccessToken().getToken();//AuthToken

                Profile userProfile = Profile.getCurrentProfile();
                if (userProfile != null){
                    setUpImageAndInfo(userProfile);
                }else{
                    Log.d(TAG,"Profile is Null");
                }
                //mUsername = mFacebookUserID;
                facebookLogin();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel");
               // mInfo.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "onError");
               // mInfo.setText("Login attempt failed.");
            }
        });
    }

    public void facebookLogin(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                                //JSONObject userInfoResponse = new JSONObject(message);
                                mUserID = message;
                               // mProfileName = userInfoResponse.getString(Constants.TAG_PROFILE_NAME);
                                mSession = SessionManager.getInstance(getApplicationContext());
                                mSession.createLoginSession(mUserID, mUsername, mProfileName);
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                startActivity(intent);
                                // this finish() method is used to tell android os that we are done with current
                                // activity now! Moving to other activity
                                // finish();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(getApplicationContext(), "Invalid username/password", "Username and password are invalid");
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
            tieInnLogin();
        }
    }

    public void tieInnLogin(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL,
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
                                mSession = SessionManager.getInstance(getApplicationContext());
                                mSession.createLoginSession(mUserID, mUsername, mProfileName);
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                startActivity(intent);
                                // this finish() method is used to tell android os that we are done with current
                                // activity now! Moving to other activity
                                // finish();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(getApplicationContext(), "Invalid username/password", "Username and password are invalid");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile userProfile = Profile.getCurrentProfile();
        if (userProfile != null){
            setUpImageAndInfo(userProfile);
        }else{
            Log.d(TAG,"Profile is Null");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        profileTracker.stopTracking();
    }

    public void setUpImageAndInfo(Profile userProfile) {
        //This method will fill up the ImageView and TextView
        // that we initialized before.
        final String userInfo = "<u>First Name:</u> " + userProfile.getFirstName() +
                "<br/><u>Last Name:</u> " + userProfile.getLastName() +
                "<br/><u>User Id:</u> " + userProfile.getId() +
                "<br/><u>Profile Link:</u> " + userProfile.getLinkUri().toString();
        Log.i(TAG, "userInfo" + userInfo);
        mProfileName = userProfile.getFirstName() + userProfile.getLastName();
        mPassword = userProfile.getFirstName();
        //profileInfoTextView.setText(Html.fromHtml(userInfo));


        //I am using the Picasso library to download the image
        // from URL and then load it to the ImageView.
       /* Picasso.with(this)
                .load("https://graph.facebook.com/" + userProfile.getId().toString() + "/picture?type=large")
                .into(profileImageView);*/
    }
}
