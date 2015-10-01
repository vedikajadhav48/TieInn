package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private String mUserID;
    private String mProfileName;
    private String mFacebookUserID;
    private String mAccessToken;
    private CallbackManager callbackManager;

    // Progress Dialog
    private ProgressDialog pDialog;

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

                mUsername = "vedikajadhav";
                mPassword = "vedika";
                normalLogin();

                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                profilePictureView.setProfileId(loginResult.getAccessToken().getUserId());*/
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
                normalLogin();
            default: break;
        }
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

    public void normalLogin(){

        /*pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        String url = Constants.LOGIN_URL + "username=" + mUsername + "&password=" + mPassword;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(JSONObject response) {
                        // success tag for json
                        try {
                            success = response.getInt(Constants.TAG_SUCCESS);
                            message = response.getString(Constants.TAG_MESSAGE);
                            if (success == 1) {
                                JSONObject userInfoResponse = new JSONObject(message);
                                //mUserID = Integer.parseInt(userInfoResponse.getString(Constants.TAG_USERID));
                                mUserID = userInfoResponse.getString(Constants.TAG_USERID);
                                mProfileName = userInfoResponse.getString(Constants.TAG_PROFILE_NAME);
                                mSession = SessionManager.getInstance(getApplicationContext());
                                mSession.createLoginSession(Integer.parseInt(mUserID), mUsername, "anroidhive@gmail.com");
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                intent.putExtra(HomeActivity.Intent_profile_name, mProfileName);
                                startActivity(intent);
                                // this finish() method is used to tell android os that we are done with current
                                // activity now! Moving to other activity
                                // finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //responseCode = json.getStatusLine().getStatusCode();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               // pDialog.hide();
            }
        }) ;

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
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
}
