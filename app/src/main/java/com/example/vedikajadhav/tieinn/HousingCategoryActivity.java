package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.vedikajadhav.tieinnLibrary.AnswerListAdapter;
import com.example.vedikajadhav.tieinnLibrary.AppController;
import com.example.vedikajadhav.tieinnLibrary.CustomRequest;
import com.example.vedikajadhav.tieinnLibrary.DiscussionListAdapter;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.example.vedikajadhav.tieinnModel.AnswerItem;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HousingCategoryActivity extends ActionBarActivity {
    private static final String TAG = "HousingCategoryACtivity";
    private EditText questionEditText;
    private Button questionPostButton;
    ListView discussionListView;
    DiscussionListAdapter discussionListAdapter;
    AnswerListAdapter answerListAdapter;
    private ArrayList<DiscussionItem> mHousingCategoryList = new ArrayList<>();
    private ArrayList<AnswerItem> mHousingAnswerList = new ArrayList<>();
    DiscussionItem newDiscussionItem = new DiscussionItem();
    private String questionToPost;
    public static final String Intent_message = "com.example.vedikajadhav.tieinn.Intent_message";
    public static final String Intent_category = "com.example.vedikajadhav.tieinn.Intent_category";
    private String message;
    private String category;
    private JSONArray questionsJSONArray;
    ProgressDialog pDialog;
    private static final String POST_QUESTION_URL = "http://tieinn.comuv.com/postQuestion.php?";

    SessionManager session;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housing_category);

        message = getIntent().getStringExtra(Intent_message);
        try {
            questionsJSONArray = new JSONArray(message);
            for(int i=0; i<questionsJSONArray.length(); i++){
                JSONObject firstQuestion = (JSONObject) questionsJSONArray.get(i);
                DiscussionItem newDiscussionItem = new DiscussionItem();
                AnswerItem newAnswerItem = new AnswerItem();
                newDiscussionItem.setDiscussionItemText(firstQuestion.getString("Question"));
                newDiscussionItem.setDiscussionCategory(firstQuestion.getString("Category"));
                mHousingCategoryList.add(0, newDiscussionItem);
                //mHousingAnswerList.add(0, newAnswerItem);
            }
            //instructorAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questionEditText = (EditText)findViewById(R.id.question_edit_text);
        questionPostButton = (Button)findViewById(R.id.question_post_button);
        discussionListView = (ListView)findViewById(R.id.discussionBoardItemList);
        discussionListAdapter = new DiscussionListAdapter(mHousingCategoryList, this);
        discussionListView.setAdapter(discussionListAdapter);

    }

    public void postQuestion(View postQuestionButton){
        questionToPost = questionEditText.getText().toString();
// Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

/*        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        // get user data from session
        session = SessionManager.getInstance(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        // id
        userID = user.get(SessionManager.KEY_USERID);
        //convert string userIDPref to int userID

        // Post params to be sent to the server
        JSONObject params = new JSONObject();
        try {
            params.put("userID", userID);
            params.put("question", questionToPost);
            params.put("category", "Housing");
        } catch (JSONException e) {
            e.printStackTrace();
        }

/*        Map<String, String> params = new HashMap<String, String>();
        params.put("userID", userID);
        params.put("question", questionToPost);
        params.put("category", "Housing");*/

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, POST_QUESTION_URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        // success tag for json
                        int success = 0;
                        String message = null;
                        try {
                            success = response.getInt("success");
                            message = response.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //responseCode = json.getStatusLine().getStatusCode();
                        Log.i(TAG, String.valueOf(success));
                        Log.i(TAG, message);
                        if (success == 1) {
                            DiscussionItem newDiscussionItem = new DiscussionItem();
                            newDiscussionItem.setDiscussionItemText(questionToPost);
                            mHousingCategoryList.add(0, newDiscussionItem);
                            discussionListAdapter.notifyDataSetChanged();
                        }
                       // pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               // pDialog.hide();
            }
        }) {

/*            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);
                params.put("question", questionToPost);
                params.put("category", "Housing");

                return params;
            }*/

/*           @Override
            public byte[] getBody() {
                return comment.getBytes();
            }*/

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_housing_category, menu);
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
