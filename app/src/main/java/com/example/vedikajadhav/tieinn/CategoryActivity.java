package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vedikajadhav.tieinnLibrary.JSONParser;
import com.example.vedikajadhav.tieinnModel.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static final String TAG= "CategoryActivity";
    private ListView categoryListView;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Get LisMenuItemView object from xml
        categoryListView = (ListView)findViewById(R.id.category_list_view);

        //Define a new Adapter
        ArrayAdapter<String> categoryListViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.category_list_menu_items));

        //Assign adapter to listView
        categoryListView.setAdapter(categoryListViewAdapter);

        //listView Item click listener
        categoryListView.setOnItemClickListener(this);
/*        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listView clicked item index
                int itemPosition = position;

                //ListView clicked item value
                String itemValue = (String)categoryListView.getItemAtPosition(position);

                switch(position){
                    case 0:
                        Intent intent0 = new Intent(getApplicationContext(), AcademicCategoryActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        //async or volley
                        //new FeedQuestions().execute();
                        getQuestionsFromNetwork("Housing");
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), DiscussionBoardActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), DiscussionBoardActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                }
            }
        });*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //listView clicked item index
        int itemPosition = position;

        //ListView clicked item value
        String category = (String)categoryListView.getItemAtPosition(position);
        Intent intent1 = new Intent(getApplicationContext(), DiscussionBoardActivity.class);
        intent1.putExtra(DiscussionBoardActivity.Intent_category, category);
        startActivity(intent1);

      /*  switch(position){
            case 0:
                Intent intent0 = new Intent(getApplicationContext(), AcademicCategoryActivity.class);
                startActivity(intent0);
                break;
            case 1:
                //async or volley
                //new FeedQuestions().execute();
                //getQuestionsFromNetwork("Housing");
                Intent intent1 = new Intent(getApplicationContext(), DiscussionBoardActivity.class);
                intent1.putExtra(DiscussionBoardActivity.Intent_category, category);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(getApplicationContext(), DiscussionBoardActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(getApplicationContext(), DiscussionBoardActivity.class);
                startActivity(intent3);
                break;
            default:
        }*/
    }

    public void getQuestionsFromNetwork(String category){
        Log.i(TAG, "Network Request for details");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.GET_QUESTIONS_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
               /* try {
                    String email = response.getString(Constants.keyEmail);
                    String phone = response.getString(Constants.keyPhone);
                    String office = response.getString(Constants.keyOffice);
                    JSONObject jsonRating = response.getJSONObject(Constants.keyRating);
                    Double averageRating = jsonRating.getDouble(Constants.keyAverageRatings);
                    int totalRatings = jsonRating.getInt(Constants.keyTotalRating);
                    mInstructor.setRating(averageRating);
                    mInstructor.setNumberOfRatings(totalRatings);
                    mInstructor.setEmail(email);
                    mInstructor.setOffice(office);
                    mInstructor.setPhone(phone);
                    mInstructor.updateInDB(getApplicationContext());
                    updateViews();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error : " + TAG, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Error Response");
            }
        });

       // NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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
                JSONObject json = jsonParser.makeHttpRequest(Constants.GET_QUESTIONS_URL, "POST", params);
                // checking log for json response
                Log.d("Registration attempt", json.toString());
                // success tag for json
                success = json.getInt(Constants.TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());
                    Intent ii = new Intent(CategoryActivity.this,DiscussionBoardActivity.class);
                    finish();
                    // this finish() method is used to tell android os that we are done with current
                    // activity now! Moving to other activity
                    ii.putExtra(DiscussionBoardActivity.Intent_message, json.getString(Constants.TAG_MESSAGE));
                    startActivity(ii);
                    return json.getString(Constants.TAG_MESSAGE);
                }else{
                    return json.getString(Constants.TAG_MESSAGE);
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
