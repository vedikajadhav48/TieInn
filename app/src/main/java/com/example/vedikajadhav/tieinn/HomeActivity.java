package com.example.vedikajadhav.tieinn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.tv.TvInputService;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

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
        //FacebookSdk.sdkInitialize(getApplicationContext());
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
}
