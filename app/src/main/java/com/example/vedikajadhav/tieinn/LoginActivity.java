package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.vedikajadhav.tieinnLibrary.DatabaseHandler;
import com.example.vedikajadhav.tieinnLibrary.JSONParser;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.example.vedikajadhav.tieinnLibrary.Utils;
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
import java.util.List;


public class LoginActivity extends ActionBarActivity{
    private static final String TAG= "LoginActivity";
    private EditText mUserNameEditText = null;
    private EditText mPasswordEditText = null;
    private String username;
    private String password;
    private TextView mSignUpTextView;
    private Button mLoginButton;
    private LoginButton mFacebookLoginButton;
    private TextView mForgotPasswordTextView;
    private Handler mLoginHandler = new Handler();
    private static final int Intent_User_Index = 123;
    private static String KEY_SUCCESS = "message";
    private static final LoginResult EXTRA_LOGIN_RESULT = null;
    private int userID;
    private String fb_user_id;
    private String access_Token;

    private TextView info;
    private CallbackManager callbackManager;

    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://tieinn.comuv.com/login.php?";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PROFILE_NAME = "profileName";
    private static final String TAG_USERID = "userID";

    // Session Manager Class
    SessionManager session;
    int responseCode = 0;

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

        //facebook_login_button.setText("Log In");

        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "onSuccess");
                fb_user_id = loginResult.getAccessToken().getUserId();
                access_Token = loginResult.getAccessToken().getToken();

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
                //home.putExtra(EXTRA_LOGIN_RESULT, loginResult);
                home.putExtra(HomeActivity.Intent_fb_user_id, fb_user_id);
               // startActivityForResult(createAccountIntent, Intent_User_Index);
                startActivity(home);


            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel");
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "onError");
                info.setText("Login attempt failed.");
            }
        });
    }

    public void login(View button){
        Log.i(TAG, "Login through tieIn app");
        //if((!userNameEditText.getText().toString().equals("")) && (!passwordEditText.getText().toString().equals(""))){
         /* if(true){
           // NetAsync(button);
            mLoginHandler.post(new LoginThread());
        }else if(mUserNameEditText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),
                    "Username field empty", Toast.LENGTH_SHORT).show();
        }else if(mPasswordEditText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),
                    "Password field empty", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "Username and Password field empty", Toast.LENGTH_SHORT).show();
        }*/
        username = mUserNameEditText.getText().toString();
        password = mPasswordEditText.getText().toString();

       // session.createLoginSession("1", username, "anroidhive@gmail.com");
        switch (button.getId()) {
            case R.id.login_button:
                 new AttemptLogin().execute();
                 // here we have used, switch case, because on login activity you may
                 // also want to show registration button, so if the user is new ! we can go the
                 // registration activity , other than this we could also do this without switch case.
            default: break;
        }
        // Session Manager
/*        session = SessionManager.getInstance(getApplicationContext());
        session.createLoginSession(userID, username, "anroidhive@gmail.com");*/
    }

    public void signUp(View button){
        Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
        startActivity(createAccountIntent);
    }

    public void forgotPassword(View button){
        Log.i(TAG, "Forgot Password");
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/

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
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest( LOGIN_URL, "POST", params);
            // checking log for json response
            Log.d("Login attempt", json.toString());

            // success tag for json
            success = json.getInt(TAG_SUCCESS);
            userID = json.getInt(TAG_USERID);

            //responseCode = json.getStatusLine().getStatusCode();
            if (success == 1) {
                Log.d("Successfully Login!", json.toString());
                Intent ii = new Intent(LoginActivity.this,HomeActivity.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ii.putExtra(HomeActivity.Intent_profile_name, json.getString(TAG_PROFILE_NAME));
                startActivity(ii);
                finish();
                // this finish() method is used to tell android os that we are done with current
                // activity now! Moving to other activity
                return json.getString(TAG_PROFILE_NAME);
            }else{
                return json.getString(TAG_PROFILE_NAME);
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
        session = SessionManager.getInstance(getApplicationContext());
        session.createLoginSession(userID, username, "anroidhive@gmail.com");
        if (message != null){
            //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}
}
