package com.example.vedikajadhav.tieinn;

import android.content.Intent;
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

import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;


public class HomeActivity extends ActionBarActivity {
    private static final String TAG= "HomeActivity";
    private ProfilePictureView profilePictureView;
    private String fbUserId;
    public static final String Intent_fb_user_id = "com.example.vedikajadhav.tieinn.Intent_fb_user_id";
    private String profileName;
    public static final String Intent_profile_name = "com.example.vedikajadhav.tieinn.Intent_profile_name";
    private Bitmap fbProfilePicBitmap;
    private ImageView imageProfileView;
    private TextView profileNameTextView;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get LisMenuItemView object from xml
        listView = (ListView)findViewById(R.id.listView);

        //Define a new Adapter
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.list_menu_items));

        //Assign adapter to listView
        listView.setAdapter(listViewAdapter);

        //listView Item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listView clicked item index
                int itemPosition = position;

                //ListView clicked item value
                String itemValue = (String)listView.getItemAtPosition(position);

                //show alert
                Toast.makeText(getApplicationContext(), "Position"+ itemPosition+ "ListItem" + itemValue, Toast.LENGTH_LONG).show();
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
                        Intent intent3 = new Intent(getApplicationContext(), LogoutActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                }
               // Intent intent = new Intent(getApplicationContext(), .class);
                //intent.setClass()
            }
        });
        // profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture_view);

        // this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_home, null);

        // this.setContentView(root);

        profileName = getIntent().getStringExtra(Intent_profile_name);
        fbUserId = getIntent().getStringExtra(Intent_fb_user_id);

        imageProfileView = (ImageView) findViewById(R.id.image_profile_view);
        profileNameTextView = (TextView) findViewById(R.id.profile_name_text);

        profileNameTextView.setText("Welcome, " + profileName + "!");
        Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + fbUserId+ "/picture?type=large").into(imageProfileView);

        //profileNameTextView = (TextView) findViewById(R.id.profile_name_text);
        //profilePictureView.setProfileId(fbUserId);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
