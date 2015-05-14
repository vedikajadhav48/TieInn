package com.example.vedikajadhav.tieinn;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends ActionBarActivity {
    private static final String TAG= "LoginActivity";
    private EditText userNameEditText;
    private EditText passwordEditText;
    private String userName;
    private String password;
    private TextView mSignUpTextView;
    private LoginButton facebook_login_button;
    private static final int Intent_User_Index = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        userNameEditText = (EditText)findViewById(R.id.edit_text_username);
        passwordEditText = (EditText)findViewById(R.id.edit_text_password);
        userName = userNameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        facebook_login_button = (LoginButton)findViewById(R.id.facebook_login_button);
        mSignUpTextView = (TextView)findViewById(R.id.sign_up_text_view);
        //facebook_login_button.setText("Log In");
    }

    public void signUp(View button){
        Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
        startActivityForResult(createAccountIntent, Intent_User_Index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
