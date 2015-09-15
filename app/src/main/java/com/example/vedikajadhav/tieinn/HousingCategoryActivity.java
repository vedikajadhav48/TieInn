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
    private EditText mQuestionEditText;
    private Button mQuestionPostButton;
    ListView mDiscussionListView;
    DiscussionListAdapter mDiscussionListAdapter;
    AnswerListAdapter mAnswerListAdapter;
    private ArrayList<DiscussionItem> mHousingDiscussionList = new ArrayList<>();
    private ArrayList<AnswerItem> mHousingAnswerList = new ArrayList<>();
    DiscussionItem mDiscussionItem = new DiscussionItem();
    AnswerItem mAnswerItem = new AnswerItem();
    private String mQuestionToPost;
    public static final String Intent_message = "com.example.vedikajadhav.tieinn.Intent_message";
    public static final String Intent_category = "com.example.vedikajadhav.tieinn.Intent_category";
    private String mMessage;
    private String mCategory;
    private JSONArray mQuestionsJSONArray;
    ProgressDialog pDialog;
    private static final String POST_QUESTION_URL = "http://tieinn.comuv.com/postQuestion.php?";

    SessionManager mSession;
    String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housing_category);

        mQuestionEditText = (EditText)findViewById(R.id.question_edit_text);
        mQuestionPostButton = (Button)findViewById(R.id.question_post_button);
        mDiscussionListView = (ListView)findViewById(R.id.housing_discussion_board_item_list);

        mMessage = getIntent().getStringExtra(Intent_message);
        try {
            mQuestionsJSONArray = new JSONArray(mMessage);
            for(int i=0; i<mQuestionsJSONArray.length(); i++){
                JSONObject question = (JSONObject) mQuestionsJSONArray.get(i);
                mDiscussionItem = new DiscussionItem();
                mDiscussionItem.setDiscussionItemText(question.getString("Question"));
                mDiscussionItem.setDiscussionCategory(question.getString("Category"));
                mHousingDiscussionList.add(0, mDiscussionItem);
                //mHousingAnswerList.add(0, newAnswerItem);
            }
            //instructorAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDiscussionListAdapter = new DiscussionListAdapter(mHousingDiscussionList, this);
        mDiscussionListView.setAdapter(mDiscussionListAdapter);

    }

    public void postQuestion(View postQuestionButton){
        mQuestionToPost = mQuestionEditText.getText().toString();

/*        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        // get user data from session
        mSession = SessionManager.getInstance(getApplicationContext());
        HashMap<String, String> user = mSession.getUserDetails();
        mUserID = user.get(SessionManager.KEY_USERID);

        // Post params to be sent to the server
        JSONObject params = new JSONObject();
        try {
            params.put("userID", mUserID);
            params.put("question", mQuestionToPost);
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
                            newDiscussionItem.setDiscussionItemText(mQuestionToPost);
                            mHousingDiscussionList.add(0, newDiscussionItem);
                            mDiscussionListAdapter.notifyDataSetChanged();
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
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
