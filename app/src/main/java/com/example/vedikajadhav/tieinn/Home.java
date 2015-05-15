package com.example.vedikajadhav.tieinn;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.vedikajadhav.view.viewgroup.FlyOutContainer;


public class Home extends ActionBarActivity {

    FlyOutContainer root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_home, null);

        this.setContentView(root);
    }


    public void toggleMenu(View v){
        this.root.toggleMenu();
    }
}
