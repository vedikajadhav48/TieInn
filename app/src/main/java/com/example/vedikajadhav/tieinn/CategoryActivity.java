package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vedikajadhav.tieinnLibrary.JSONParser;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends ActionBarActivity {
    private ListView categoryListView;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String QUESTION_FEED_URL = "http://tieinn.comuv.com/getQuestion.php?";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_QUESTION = "question";
    private static final String TAG_CATEGORY = "category";

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
                //Toast.makeText(getApplicationContext(), "Position" + itemPosition + "ListItem" + itemValue, Toast.LENGTH_LONG).show();
                switch(position){
                    case 0:
                        Intent intent0 = new Intent(getApplicationContext(), AcademicCategoryActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        new FeedQuestions().execute();
                        //Intent intent1 = new Intent(getApplicationContext(), HousingCategoryActivity.class);
                        //startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), HousingCategoryActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), HousingCategoryActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                }
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

    class FeedQuestions extends AsyncTask<String, String, String> {
        /** * Before starting background thread Show Progress Dialog * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CategoryActivity.this);
            pDialog.setMessage("Feeding Questions...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("category", "Housing"));
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest( QUESTION_FEED_URL, "POST", params);
                // checking log for json response
                Log.d("Registration attempt", json.toString());
                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());
                    Intent ii = new Intent(CategoryActivity.this,HousingCategoryActivity.class);
                    finish();
                    // this finish() method is used to tell android os that we are done with current
                    // activity now! Moving to other activity
                    ii.putExtra(HousingCategoryActivity.Intent_question, json.getString(TAG_MESSAGE));
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }else{
                    return json.getString(TAG_MESSAGE);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /** * Once the background process is done we need to Dismiss the progress dialog asap * **/
        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null){
                Log.i("messageJSONARRAY", message);
            }
        }
    }
}
