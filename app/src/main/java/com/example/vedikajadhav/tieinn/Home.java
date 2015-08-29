package com.example.vedikajadhav.tieinn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.vedikajadhav.view.viewgroup.FlyOutContainer;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

public class Home extends ActionBarActivity {
    private static final String TAG= "Home";
    FlyOutContainer root;
    private ProfilePictureView profilePictureView;
    private String fbUserId;
    public static final String Intent_fb_user_id = "com.example.vedikajadhav.tieinn.Intent_fb_user_id";
    private Bitmap fbProfilePicBitmap;
    private ImageView imageProfileView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture_view);

        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_home, null);

        this.setContentView(root);

        fbUserId = getIntent().getStringExtra(Intent_fb_user_id);
        imageProfileView = (ImageView) findViewById(R.id.image_profile_view);
        Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + fbUserId+ "/picture?type=large").into(imageProfileView);
    }
    public void toggleMenu(View v){
        this.root.toggleMenu();
    }
}
