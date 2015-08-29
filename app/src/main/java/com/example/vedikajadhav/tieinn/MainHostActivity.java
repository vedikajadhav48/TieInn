package com.example.vedikajadhav.tieinn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainHostActivity extends ActionBarActivity implements ListItemFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener{
    public static final String Intent_fb_user_id = "com.example.vedikajadhav.tieinn.MainHostActivity.Intent_fb_user_id";
    private String fbUserId;
    private ProfilePictureView profilePictureView;
    private ImageView imageProfileView;
    private Bitmap fbProfilePicBitmap;
    private TextView profileNameTextView;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_host);
        fbUserId = getIntent().getStringExtra(Intent_fb_user_id);
       // profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture_view);
        imageProfileView = (ImageView) findViewById(R.id.image_profile_view);
        profileNameTextView = (TextView) findViewById(R.id.profile_name_text);
        //profilePictureView.setProfileId(fbUserId);
        Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + fbUserId+ "/picture?type=large").into(imageProfileView);
        profileNameTextView.setText(fbUserId);
        /*try {
            fbProfilePicBitmap = getFacebookProfilePicture(fbUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
       // profilePictureView.setDefaultProfilePicture(fbProfilePicBitmap);
        //setContentView(R.layout.activity_main_host);
    }

    public static Bitmap getFacebookProfilePicture(String userID)
            throws Exception {

        Bitmap bitmap = null;
        URL url = new URL("https://graph.facebook.com/" + userID + "/picture?type=normal");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(in);
        }
        finally {
            urlConnection.disconnect();
        }
        return bitmap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_host, menu);
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
