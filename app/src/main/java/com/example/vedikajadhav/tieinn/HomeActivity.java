package com.example.vedikajadhav.tieinn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class HomeActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static final String TAG= "HomeActivity";
    private ProfilePictureView mProfilePictureView;
    private String mFacebookUserID;
    private String mUserID;
    private String mProfileName;
    private String mUsername;
    private ImageView mProfileImageView;
    private TextView mProfileNameTextView;
    ListView mainListView;
    SessionManager mSession;
    public static final String Intent_fb_user_id = "com.example.vedikajadhav.tieinn.Intent_fb_user_id";
    public static final String Intent_profile_name = "com.example.vedikajadhav.tieinn.Intent_profile_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mUserID = user.get(SessionManager.KEY_USERID);
        mUsername = user.get(SessionManager.KEY_NAME);

        mainListView = (ListView)findViewById(R.id.main_list_view);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.list_menu_items));
        mainListView.setAdapter(listViewAdapter);

        //listView Item click listener
        mainListView.setOnItemClickListener(this);

        mProfileName = getIntent().getStringExtra(Intent_profile_name);
        mFacebookUserID = getIntent().getStringExtra(Intent_fb_user_id);

        mProfileImageView = (ImageView) findViewById(R.id.image_profile_view);
        mProfileNameTextView = (TextView) findViewById(R.id.profile_name_text);

        mProfileNameTextView.setText("Welcome, " + mProfileName + "!");
        Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + mFacebookUserID + "/picture?type=large").into(mProfileImageView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //listView clicked item index
        int itemPosition = position;

        //ListView clicked item value
        String itemValue = (String)mainListView.getItemAtPosition(position);

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
                break;
            default:
        }
    }

    public void close(View view){
        finish();
    }
}
