package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputService;
import android.service.textservice.SpellCheckerService;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    private String mFacebookUserID;
    private String mAccessToken;
    private CallbackManager callbackManager;
    SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        mUserNameEditText = (EditText)findViewById(R.id.edit_text_username_to_login);
        mPasswordEditText = (EditText)findViewById(R.id.edit_text_password_to_login);
        mFacebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);

        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mFacebookUserID = loginResult.getAccessToken().getUserId();//UserID
                mAccessToken = loginResult.getAccessToken().getToken();//AuthToken
               // loginResult.getAccessToken().

                mUsername = "vedikajadhav";
                mPassword = "vedika";
                tieInnLogin(getApplicationContext(), mUsername, mPassword);
               /* GraphRequest request = GraphRequest.newMeRequest(
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

                /*Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                home.putExtra(HomeActivity.Intent_fb_user_id, mFacebookUserID);
                home.putExtra(HomeActivity.Intent_profile_name, "FacebookUser");
                startActivity(home);*/
                // make request to get facebook user info
                /*GraphRequest.executeMeRequestAsync(session, new GraphRequest.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        Log.i("fb", "fb user: "+ user.toString());

                        String fbId = user.getId();
                        String fbAccessToken = fbAccessToken;
                        String fbName = user.getName();
                        String gender = user.asMap().get("gender").toString();
                        String email = user.asMap().get("email").toString();

                        Log.i("fb", userProfile.getEmail());
                    }
                });*/
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

    public void login(View button){
        mUsername = mUserNameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        if((mUsername.equals("")) || (mPassword.equals(""))){
            CustomAlertDialog.showAlertDialog(this, "Username and/or Password field empty", "Enter Username and Password");
        }else {
            tieInnLogin(this, mUsername, mPassword);
        }
    }

    public void tieInnLogin(final Context context, final String username, final String password){
        RequestQueue queue = Volley.newRequestQueue(context);
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
                                CustomAlertDialog.showAlertDialog(context, "Invalid username/password", "Username and password are invalid");
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
                params.put("username", username);
                params.put("password", password);
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

/*    private class SessionStatusCallback implements Session.StatusCallback {
        private String fbAccessToken;

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
            if (session.isOpened()) {
                fbAccessToken = session.getAccessToken();
                // make request to get facebook user info
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        Log.i("fb", "fb user: "+ user.toString());

                        String fbId = user.getId();
                        String fbAccessToken = fbAccessToken;
                        String fbName = user.getName();
                        String gender = user.asMap().get("gender").toString();
                        String email = user.asMap().get("email").toString();

                        Log.i("fb", userProfile.getEmail());
                    }
                });
            }
        }
    }*/

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
}
