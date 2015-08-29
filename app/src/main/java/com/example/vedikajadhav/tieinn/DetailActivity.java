package com.example.vedikajadhav.tieinn;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vedikajadhav.tieinn.dummy.DummyContent;


public class DetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            DetailFragment details = new DetailFragment();
            details.setArguments(getIntent().getExtras());
            /*getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, details).commit();*/
            //getSupportFragmentManager().beginTransaction().add(R.id.list, details).commit();
        }
    }
}
