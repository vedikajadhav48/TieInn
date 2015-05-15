package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.vedikajadhav.tieinnLibrary.UserFunctions;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends ActionBarActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mUserNameEditText = (EditText)findViewById(R.id.edit_text_username_to_login);
        mPasswordEditText = (EditText)findViewById(R.id.edit_text_password_to_login);
       // username = userNameEditText.getText().toString();
       // password = passwordEditText.getText().toString();
        mLoginButton = (Button)findViewById(R.id.login_button);
        mFacebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        mSignUpTextView = (TextView)findViewById(R.id.sign_up_text_view);
        mForgotPasswordTextView = (TextView)findViewById(R.id.forgot_password_text_view);
        //facebook_login_button.setText("Log In");
    }

    public void login(View button){
        Log.i(TAG, "Login through tieIn app");
        //if((!userNameEditText.getText().toString().equals("")) && (!passwordEditText.getText().toString().equals(""))){
          if(true){
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
        }
    }

    public void signUp(View button){
        Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
        startActivityForResult(createAccountIntent, Intent_User_Index);
    }

    public void forgotPassword(View button){
        Log.i(TAG, "Forgot Password");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class LoginThread implements Runnable{

        @Override
        public void run() {
            Log.i(TAG, "Logn Thread getting executed");
        }
    }
}
