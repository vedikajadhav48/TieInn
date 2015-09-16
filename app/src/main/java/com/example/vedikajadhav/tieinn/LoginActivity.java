package com.example.vedikajadhav.tieinn;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vedikajadhav.tieinnLibrary.AppController;
import com.example.vedikajadhav.tieinnLibrary.CustomAlertDialog;
import com.example.vedikajadhav.tieinnLibrary.JSONParser;
import com.example.vedikajadhav.tieinnLibrary.NetworkRequest;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.example.vedikajadhav.tieinnLibrary.Util;
import com.example.vedikajadhav.tieinnModel.Constants;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends ActionBarActivity{
    private static final String TAG= "LoginActivity";
    private EditText mUserNameEditText = null;
    private EditText mPasswordEditText = null;
    private String mUsername;
    private String mPassword;
    private TextView mSignUpTextView;
    private Button mLoginButton;
    private LoginButton mFacebookLoginButton;
    private TextView mForgotPasswordTextView;
    private int mUserID;
    private String mProfileName;
    private String mFacebookUserID;
    private String mAccessToken;
    private CallbackManager callbackManager;

    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // Session Manager Class
    SessionManager mSession;
    private int mResponseCode = 0;
    private TextView mInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        mUserNameEditText = (EditText)findViewById(R.id.edit_text_username_to_login);
        mPasswordEditText = (EditText)findViewById(R.id.edit_text_password_to_login);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mFacebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        mSignUpTextView = (TextView)findViewById(R.id.sign_up_text_view);
        mForgotPasswordTextView = (TextView)findViewById(R.id.forgot_password_text_view);

        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mFacebookUserID = loginResult.getAccessToken().getUserId();
                mAccessToken = loginResult.getAccessToken().getToken();

                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                profilePictureView.setProfileId(loginResult.getAccessToken().getUserId());*/
                /*GraphRequest request = GraphRequest.newMeRequest(
                        access_Token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();*/

                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                home.putExtra(HomeActivity.Intent_fb_user_id, mFacebookUserID);
                startActivity(home);


            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel");
                mInfo.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "onError");
                mInfo.setText("Login attempt failed.");
            }
        });
    }

    public void login(View button){
        mUsername = mUserNameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        if((mUsername.equals("")) || (mPassword.equals(""))){
            CustomAlertDialog.showAlertDialog(this, "Username and/or Password field empty", "Enter Username and Password");
        }

        // here we have used, switch case, because on login activity you may
        // also want to show registration button, so if the user is new ! we can go the
        // registration activity , other than this we could also do this without switch case.
        switch (button.getId()) {
            case R.id.login_button:
                //call async or volley
                 new AttemptLogin().execute();
                //normalLogin();
            default: break;
        }
    }

    public void normalLogin(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
       // NetworkRequest networkRequest = NetworkRequest.getInstance(getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.LOGIN_URL, null,
                new Response.Listener<JSONObject>() {
                    // here Check for success tag
                    int success;
                    String message;
                    JSONObject userInfoResponse;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        // success tag for json
                        try {
                            success = response.getInt(Constants.TAG_SUCCESS);
                            message = response.getString(Constants.TAG_MESSAGE);
                            userInfoResponse = new JSONObject(message);
                            mUserID = Integer.parseInt(userInfoResponse.getString(Constants.TAG_USERID));
                            mProfileName = userInfoResponse.getString(Constants.TAG_PROFILE_NAME);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //responseCode = json.getStatusLine().getStatusCode();
                        if (success == 1) {
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(HomeActivity.Intent_profile_name, mProfileName);
                            startActivity(intent);
                            // this finish() method is used to tell android os that we are done with current
                            // activity now! Moving to other activity
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mUsername);
                params.put("password", mPassword);

                return params;
            }
        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
        NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
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

class AttemptLogin extends AsyncTask<String, String, String> {
    /** * Before starting background thread Show Progress Dialog * */
    boolean failure = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Attempting for login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
    // TODO Auto-generated method stub
    // here Check for success tag
        int success;
        String message;
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", mUsername));
            params.add(new BasicNameValuePair("password", mPassword));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(Constants.LOGIN_URL, "POST", params);
            // checking log for json response
            Log.d("Login attempt", json.toString());

            // success tag for json
            success = json.getInt(Constants.TAG_SUCCESS);
            message = json.getString(Constants.TAG_MESSAGE);
            JSONObject user = new JSONObject(message);
            mUserID = Integer.parseInt(user.getString(Constants.TAG_USERID));

            //responseCode = json.getStatusLine().getStatusCode();
            if (success == 1) {
                Log.d("Successfully Login!", json.toString());
                Intent ii = new Intent(LoginActivity.this,HomeActivity.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ii.putExtra(HomeActivity.Intent_profile_name, user.getString(Constants.TAG_PROFILE_NAME));
                startActivity(ii);
                finish();
                // this finish() method is used to tell android os that we are done with current
                // activity now! Moving to other activity
                return user.getString(Constants.TAG_PROFILE_NAME);
            }else{
                return user.getString(Constants.TAG_PROFILE_NAME);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** * Once the background process is done we need to Dismiss the progress dialog asap * **/
    protected void onPostExecute(String message) {
        pDialog.dismiss();
        mSession = SessionManager.getInstance(getApplicationContext());
        mSession.createLoginSession(mUserID, mUsername, "anroidhive@gmail.com");
        if (message != null){
            //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}
}
