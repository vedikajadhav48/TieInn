package com.example.vedikajadhav.tieinn;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class CategoryActivity extends ActionBarActivity {
    private ListView categoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Get LisMenuItemView object from xml
        categoryListView = (ListView)findViewById(R.id.categoryListView);

        //Define a new Adapter
        ArrayAdapter<String> categoryListViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.category_list_menu_items));

        //Assign adapter to listView
        categoryListView.setAdapter(categoryListViewAdapter);

        //listView Item click listener
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listView clicked item index
                int itemPosition = position;

                //ListView clicked item value
                String itemValue = (String)categoryListView.getItemAtPosition(position);

                //show alert
                Toast.makeText(getApplicationContext(), "Position" + itemPosition + "ListItem" + itemValue, Toast.LENGTH_LONG).show();
                /*switch(position){
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
                }*/
                // Intent intent = new Intent(getApplicationContext(), .class);
                //intent.setClass()
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
