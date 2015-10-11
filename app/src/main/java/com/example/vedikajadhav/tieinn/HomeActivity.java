package com.example.vedikajadhav.tieinn;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import java.util.HashMap;

public class HomeActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static final String TAG= "HomeActivity";
    private String mProfileName;
    private TextView mProfileNameTextView;
    ListView mainListView;
    SessionManager mSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Vedika HomeActivity: onCreate");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        // Session class instance
        mSession = SessionManager.getInstance(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(mSession.checkLogin()) {
            finish();
        }

        // get user data from session
        HashMap<String, String> user = mSession.getUserDetails();
        mProfileName = user.get(SessionManager.KEY_PROFILE_NAME);

        mainListView = (ListView)findViewById(R.id.main_list_view);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.list_menu_items));
        mainListView.setAdapter(listViewAdapter);
        mainListView.setOnItemClickListener(this);

        mProfileNameTextView = (TextView) findViewById(R.id.profile_name_text);
        mProfileNameTextView.setText("Welcome, " + mProfileName + "!");
      //  Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + mFacebookUserID + "/picture?type=large").into(mProfileImageView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch(position){
            case 0:
                Intent intent0 = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(intent2);
                break;
            case 3:
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                mSession.logoutUser();
                LoginManager.getInstance().logOut();
           /*     Session session = Session.getActiveSession();
                if (session != null) {

                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                        //clear your preferences if saved
                    }
                } else {

                    session = new TvInputService.Session(context);
                    Session.setActiveSession(session);

                    session.closeAndClearTokenInformation();
                    //clear your preferences if saved

                }*/
                finish();
                break;
            default:
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "Vedika HomeActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Vedika HomeActivity: onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "Vedika HomeActivity: onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Vedika HomeActivity: onStop");
        //profileTracker.stopTracking();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Vedika HomeActivity: onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Vedika HomeActivity: onDestroy");
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Log.i(TAG, "Vedika HomeActivity: onBackPressed");
        moveTaskToBack(true);
        return;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "Vedika HomeActivity: onSaveInstanceState");
    }
}
