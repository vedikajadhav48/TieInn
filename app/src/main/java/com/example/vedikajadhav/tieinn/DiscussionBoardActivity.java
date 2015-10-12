package com.example.vedikajadhav.tieinn;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vedikajadhav.tieinnLibrary.AppController;
import com.example.vedikajadhav.tieinnLibrary.CustomAlertDialog;
import com.example.vedikajadhav.tieinnLibrary.DiscussionExpandableListAdapter;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.example.vedikajadhav.tieinnLibrary.Util;
import com.example.vedikajadhav.tieinnModel.AnswerItem;
import com.example.vedikajadhav.tieinnModel.Constants;
import com.example.vedikajadhav.tieinnModel.QuestionItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscussionBoardActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "DiscussionBoardActivity";
    private EditText mQuestionEditText;
    private Button mQuestionPostButton;
    private String mQuestionToPost;
    ExpandableListView mDiscussionExpandableListView;
    DiscussionExpandableListAdapter mDiscussionExpandableListAdapter;
    List<QuestionItem> mQuestionList = new ArrayList<QuestionItem>();
    HashMap<Integer, List<AnswerItem>> mAnswerList = new HashMap<Integer, List<AnswerItem>>();
    public static final String Intent_category = "com.example.vedikajadhav.tieinn.Intent_category";
    private String mCategory;
    SessionManager mSession;
    String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_board);

        mQuestionEditText = (EditText)findViewById(R.id.question_edit_text);
        mQuestionPostButton = (Button)findViewById(R.id.question_post_button);
        mQuestionPostButton.setOnClickListener(this);
        mDiscussionExpandableListView = (ExpandableListView)findViewById(R.id.discussion_expandable_list_view);
        mCategory = getIntent().getStringExtra(Intent_category);

        // Session class instance
        mSession = SessionManager.getInstance(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(mSession.checkLogin()) {
            finish();
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setSubtitle(mCategory);
        }
        // get user data from session
        HashMap<String, String> user = mSession.getUserDetails();
        mUserID = user.get(SessionManager.KEY_USERID);

        if (Util.isNetworkAvailable(getApplicationContext())) {
            getQuestionsFromNetwork();
        }
    }

    public void getQuestionsFromNetwork(){
       // Log.i(TAG, "Network Request for questions");
        String url = Constants.GET_QUESTIONS_URL + "userID=" + mUserID + "&category=" + mCategory;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            int success;
            String message;
            JSONArray mQuestionsJSONArray;

            @Override
            public void onResponse(JSONObject response) {
                try {
                    success = response.getInt(Constants.TAG_SUCCESS);
                    message = response.getString(Constants.TAG_MESSAGE);

                    if (success == 1) {
                        Log.d("questions fetched!", message);
                        mQuestionsJSONArray = new JSONArray(message);
                        for (int i = 0; i < mQuestionsJSONArray.length(); i++) {
                            JSONObject question = (JSONObject) mQuestionsJSONArray.get(i);
                            QuestionItem mQuestionItem = new QuestionItem();
                            mQuestionItem.setQuestionItemID(question.getInt("QuestionID"));
                            mQuestionItem.setQuestionItemUserID(question.getString("QuestionUserID"));
                            mQuestionItem.setQuestionItemText(question.getString("Question"));
                            mQuestionItem.setQuestionItemCategory(question.getString("Category"));
                            mQuestionItem.setQuestionItemDate(question.getString("QuestionDate"));
                            mQuestionList.add(0, mQuestionItem);
                        }
                        //Toast.makeText(getApplicationContext(), "Success Questions: " + TAG, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               getAnswersFromNetwork();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Question: " + TAG, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Error Response");
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void getAnswersFromNetwork(){
       // Log.i(TAG, "Network Request for answers");
        final JSONArray jsonQuestionsArray = new JSONArray();
        for (int i=0; i < mQuestionList.size(); i++) {
            jsonQuestionsArray.put(mQuestionList.get(i).getJSONObject());
        }

        // the parameters for the php
        Map<String, String> map = new HashMap<String, String>();
        map.put("userID", mUserID);
        map.put("jsonQuestionsArray", String.valueOf(jsonQuestionsArray));
        JSONObject params = new JSONObject(map);

        JsonObjectRequest answerJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_ALL_ANSWERS_URL, params, new Response.Listener<JSONObject>() {
            int success;
            String message;
            JSONArray mAnswersJSONArray;

            @Override
            public void onResponse(JSONObject response) {
                try {
                    success = response.getInt(Constants.TAG_SUCCESS);
                    message = response.getString(Constants.TAG_MESSAGE);

                    if (success == 1) {
                        mAnswersJSONArray = new JSONArray(message);
                        for (int i = 0; i < mAnswersJSONArray.length(); i++) {
                            JSONArray answerArrayPerQuestionID = (JSONArray) mAnswersJSONArray.get(i);
                            JSONObject answerJSONObject = new JSONObject();
                            List<AnswerItem> answers = new ArrayList<AnswerItem>();
                                for (int j = 0; j < answerArrayPerQuestionID.length(); j++) {
                                    answerJSONObject = (JSONObject) answerArrayPerQuestionID.get(j);
                                    AnswerItem mAnswerItem = new AnswerItem();
                                    mAnswerItem.setAnswerItemID(answerJSONObject.getInt("AnswerID"));
                                    mAnswerItem.setAnswerItemUserID(answerJSONObject.getString("AnswerUserID"));
                                    mAnswerItem.setQuestionID(answerJSONObject.getInt("QuestionID"));
                                    mAnswerItem.setAnswerItemText(answerJSONObject.getString("Answer"));
                                    mAnswerItem.setAnswerRecommendCount(answerJSONObject.getInt("NumberOfRecommendations"));
                                    mAnswerItem.setAnswerItemDate(answerJSONObject.getString("AnswerDate"));
                                    answers.add(mAnswerItem);
                                }
                                if(answerArrayPerQuestionID.length()==0){
                                    mAnswerList.put(mQuestionList.get(i).getQuestionItemID(), answers);
                                }else {
                                    mAnswerList.put(answerJSONObject.getInt("QuestionID"), answers);
                                }
                        }
                     //   Toast.makeText(getApplicationContext(), "Success Answers: " + TAG, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateExpandableListView();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /* here you can warn the user that there
                      was an error while trying to get the json
                      information from the php  */
                Toast.makeText(getApplicationContext(), "Error Answers: " + TAG, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Error Response", error);
            }
        });

        AppController.getInstance().addToRequestQueue(answerJsonObjectRequest);
    }

    public void updateExpandableListView(){
        mDiscussionExpandableListAdapter = new DiscussionExpandableListAdapter(this, mQuestionList, mAnswerList);
        mDiscussionExpandableListView.setAdapter(mDiscussionExpandableListAdapter);

        // Listview Group click listener
        mDiscussionExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        /*mDiscussionExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if(parent.isGroupExpanded(groupPosition)){
                    parent.collapseGroup(groupPosition);
                }else{
                    boolean animateExpansion = false;
                    parent.expandGroup(groupPosition,animateExpansion);
                }
                //telling the listView we have handled the group click, and don't want the default actions.
                return true;
            }
        });*/

        // Listview on child click listener
   /*     mQuestionExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        mHousingQuestionList.get(groupPosition)
                                + " : "
                                + mHousingAnswerList.get(
                                mHousingQuestionList.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // Listview Group expanded listener
        mQuestionExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        mHousingQuestionList.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        mQuestionExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        mHousingQuestionList.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });*/
    }

    public void postQuestionToNetwork(final Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.POST_QUESTION_URL,
                new Response.Listener<String>() {
                    // here Check for success tag
                    int success;
                    String message;
                    List<AnswerItem> answersForPostedQuestion = new ArrayList<AnswerItem>();
                    AnswerItem mAnswerItem = new AnswerItem();

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObjectResponse = new JSONObject(response);
                            success = jsonObjectResponse.getInt("success");
                            message = jsonObjectResponse.getString("message");

                            if (success == 1) {
                                JSONObject questionJSONObject = new JSONObject(message);
                                QuestionItem newQuestionItem = new QuestionItem();
                                //newQuestionItem.setQuestionItemID(Integer.parseInt(message));
                                newQuestionItem.setQuestionItemID(questionJSONObject.getInt("QuestionID"));
                                newQuestionItem.setQuestionItemUserID(mUserID);
                                newQuestionItem.setQuestionItemText(mQuestionToPost);
                                newQuestionItem.setQuestionItemCategory(mCategory);
                                newQuestionItem.setQuestionItemDate(questionJSONObject.getString("QuestionDate"));
                                mQuestionList.add(0, newQuestionItem);
                                //answersForPostedQuestion.add(mAnswerItem);
                                //mAnswerList.put(newQuestionItem.getQuestionItemID(), answersForPostedQuestion);
                                mAnswerList.put(questionJSONObject.getInt("QuestionID"), answersForPostedQuestion);
                                mDiscussionExpandableListAdapter.notifyDataSetChanged();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Error Occurred", "Question could not be posted!!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //responseCode = json.getStatusLine().getStatusCode();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("questionUserID", mUserID);
                params.put("question", mQuestionToPost);
                params.put("category", mCategory);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (Util.isNetworkAvailable(getApplicationContext())) {
            mQuestionToPost = mQuestionEditText.getText().toString();
            if(!mQuestionToPost.equalsIgnoreCase("")){
                mQuestionEditText.setText("");
                //postQuestionToNetwork(this, mQuestionToPost, mUserID, mCategory);
                postQuestionToNetwork(this);
            }else{
                CustomAlertDialog.showAlertDialog(this, "Empty Question", "Enter a question!!");
            }
        } else {
            CustomAlertDialog.showAlertDialog(this, "Network Unavailable", "Please check network connection and try again.");
        }
    }
}
