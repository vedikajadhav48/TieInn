package com.example.vedikajadhav.tieinn;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import java.util.HashMap;

public class AccountActivity extends ActionBarActivity {
    private static final String TAG= "AccountActivity";
    SessionManager mSession;
    private String mUsername;
    private String mProfileName;
    private TextView mUsernameEditText;
    private TextView mProfileNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Session class instance
        mSession = SessionManager.getInstance(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(mSession.checkLogin()) {
            finish();
        }

        //get user data from session
        HashMap<String, String> user = mSession.getUserDetails();
        mUsername = user.get(SessionManager.KEY_USERNAME);
        mProfileName = user.get(SessionManager.KEY_PROFILE_NAME);

        mUsernameEditText = (TextView)findViewById(R.id.edit_text_account_username);
        mProfileNameEditText = (TextView)findViewById(R.id.edit_text_account_profile_name);

        mUsernameEditText.setText(mUsername);
        mProfileNameEditText.setText(mProfileName);

    }
}
