package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
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
import com.example.vedikajadhav.tieinnLibrary.DiscussionListAdapter;
import com.example.vedikajadhav.tieinnLibrary.PostCreateAccountResponseListener;
import com.example.vedikajadhav.tieinnLibrary.SessionManager;
import com.example.vedikajadhav.tieinnLibrary.Util;
import com.example.vedikajadhav.tieinnModel.AnswerItem;
import com.example.vedikajadhav.tieinnModel.Constants;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HousingCategoryActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "HousingCategoryActivity";
    private EditText mQuestionEditText;
    private Button mQuestionPostButton;
    ListView mDiscussionListView;
    ExpandableListView mDiscussionExpandableListView;
    DiscussionListAdapter mDiscussionListAdapter;
    DiscussionExpandableListAdapter mDiscussionExpandableListAdapter;
   // private ArrayList<DiscussionItem> mHousingDiscussionList = new ArrayList<>();
   // private ArrayList<AnswerItem> mHousingAnswerList = new ArrayList<>();
    List<DiscussionItem> mHousingDiscussionList = new ArrayList<DiscussionItem>();
    HashMap<String, List<AnswerItem>> mHousingAnswerList = new HashMap<>();
    private String mQuestionToPost;
    public static final String Intent_message = "com.example.vedikajadhav.tieinn.Intent_message";
    public static final String Intent_category = "com.example.vedikajadhav.tieinn.Intent_category";
    private int mSuccess;
    private String mMessage;
    private String mCategory;
    private JSONArray mQuestionsJSONArray;
    ProgressDialog pDialog;

    SessionManager mSession;
    String mUserID;
    String mQuestionID;
    private PostCreateAccountResponseListener mPostCreateAccountListener;
    private PostCreateAccountResponseListener mGetAnswersCompletedListener;
    private PostCreateAccountResponseListener mGetQuestionsCompletedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housing_category);

        mQuestionEditText = (EditText)findViewById(R.id.question_edit_text);
        mQuestionPostButton = (Button)findViewById(R.id.question_post_button);
        mQuestionPostButton.setOnClickListener(this);
        mCategory = getIntent().getStringExtra(Intent_category);
        if (Util.isNetworkAvailable(getApplicationContext())) {
            getQuestionsFromNetwork();
        /*for(int i=0; i<mHousingDiscussionList.size(); i++){
              //mHousingAnswerList.put(mHousingDiscussionList.get(i), top250); // Header, Child data
            getAnswersFromNetwork(mHousingDiscussionList.get(i).getDiscussionItemID());
            }*/
           // updateDiscussionListView();
        }

       // updateDiscussionListView();
        mPostCreateAccountListener = new PostCreateAccountResponseListener() {
            @Override
            public void requestStarted() {

            }

            @Override
            public void requestCompleted() {
                /*DiscussionItem newDiscussionItem = new DiscussionItem();
                newDiscussionItem.setDiscussionItemText(mQuestionToPost);
                mHousingDiscussionList.add(0, newDiscussionItem);
                mDiscussionListAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void requestEndedWithError(VolleyError error) {

            }
        };
    }

    public void getQuestionsFromNetwork(){
        Log.i(TAG, "Network Request for questions");
        // get user data from session
        mSession = SessionManager.getInstance(getApplicationContext());
        HashMap<String, String> user = mSession.getUserDetails();
        mUserID = user.get(SessionManager.KEY_USERID);
        String url = Constants.GET_QUESTIONS_URL + "userID=" + mUserID + "&category=Housing";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            int success;
            String message;
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
                            DiscussionItem mDiscussionItem = new DiscussionItem();
                            mDiscussionItem.setDiscussionItemText(question.getString("Question"));
                            mDiscussionItem.setDiscussionCategory(question.getString("Category"));
                            mDiscussionItem.setDiscussionItemID(question.getInt("QuestionID"));
                            //getAnswersFromNetwork(question.getInt("QuestionID"));
                            mHousingDiscussionList.add(0, mDiscussionItem);
                        }
                        Toast.makeText(getApplicationContext(), "Success Questions: " + TAG, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //mGetQuestionsCompletedListener.requestCompleted();
                /*for(int i=0; i<mHousingDiscussionList.size(); i++){
                    //mHousingAnswerList.put(mHousingDiscussionList.get(i), top250); // Header, Child data
                    //mHousingAnswerList.put(questionID, answers);
                    getAnswersFromNetwork(mHousingDiscussionList.get(i).getDiscussionItemID());
                }*/
               getAnswersFromNetwork();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Question: " + TAG, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Error Response");
            }
        });

        //NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        // getAnswersFromNetwork();
    }

    public void getAnswersFromNetwork(){
        Log.i(TAG, "Network Request for answers");
        Log.i(TAG, "mHousingDiscussionList" + mHousingDiscussionList.toString());
        JSONArray jsonQuestionsArray = new JSONArray();
        for (int i=0; i < mHousingDiscussionList.size(); i++) {
            jsonQuestionsArray.put(mHousingDiscussionList.get(i).getJSONObject());
        }
        // the parameters for the php
        Map<String, String> map = new HashMap<String, String>();
        //map.put(KEY, VALUE);
        map.put("userID", mUserID);
        map.put("jsonQuestionsArray", String.valueOf(jsonQuestionsArray));
        JSONObject params = new JSONObject(map);
        /*try {
            params.put("userID", mUserID);
            params.put("jsonQuestionsArray", jsonQuestionsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        Log.i(TAG, "params" + params);
        // get user data from session
        mSession = SessionManager.getInstance(getApplicationContext());
        HashMap<String, String> user = mSession.getUserDetails();
        mUserID = user.get(SessionManager.KEY_USERID);
        String url = Constants.GET_ALL_ANSWERS_URL;
        //String url = Constants.GET_ANSWERS_URL + "userID=" + mUserID + "&questionID=" + questionID;
       // Log.i(TAG, "["+response+"]");
        JsonObjectRequest answerJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            int success;
            String message;
            JSONArray mAnswersJSONArray;

            @Override
            public void onResponse(JSONObject response) {
                try {
                    success = response.getInt(Constants.TAG_SUCCESS);
                    message = response.getString(Constants.TAG_MESSAGE);

                    if (success == 1) {
                        Log.d("answers fetched!", message);
                        mAnswersJSONArray = new JSONArray(message);
                        for (int i = 0; i < mAnswersJSONArray.length(); i++) {
                            JSONArray answerArrayPerQuestionID = (JSONArray) mAnswersJSONArray.get(i);
                            JSONObject answerJSONObject = new JSONObject();
                            List<AnswerItem> answers = new ArrayList<AnswerItem>();
                            for(int j = 0; j < answerArrayPerQuestionID.length(); j++) {
                                answerJSONObject = (JSONObject) answerArrayPerQuestionID.get(j);
                                AnswerItem mAnswerItem = new AnswerItem();
                                mAnswerItem.setAnswerItemText(answerJSONObject.getString("Answer"));
                                //mAnswerItem.setQuestionID(Integer.parseInt(answer.getString("QuestionID")));
                                answers.add(mAnswerItem);
                            }
                            mHousingAnswerList.put(answerJSONObject.getString("QuestionID"), answers);
                        }
                        //mHousingAnswerList.put(String.valueOf(questionID), answers); // Header, Child data
                        //mHousingAnswerList.put(mAnswerItem.getQuestionID(), answers); // Header, Child data
                        Toast.makeText(getApplicationContext(), "Success Answers: " + TAG, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateDiscussionListView();
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

        //NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        AppController.getInstance().addToRequestQueue(answerJsonObjectRequest);
       // updateDiscussionListView();
    }

    public void updateDiscussionListView(){
        /*mDiscussionListView = (ListView)findViewById(R.id.discussion_expandable_list_view);
        mDiscussionListAdapter = new DiscussionListAdapter(mHousingDiscussionList, this);
        mDiscussionListView.setAdapter(mDiscussionListAdapter);*/
        mDiscussionExpandableListView = (ExpandableListView)findViewById(R.id.discussion_expandable_list_view);
        // preparing list data
       // prepareListData();
        mDiscussionExpandableListAdapter = new DiscussionExpandableListAdapter(this, mHousingDiscussionList, mHousingAnswerList, mUserID);
        mDiscussionExpandableListView.setAdapter(mDiscussionExpandableListAdapter);

        // Listview Group click listener
        mDiscussionExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                 /*Toast.makeText(getApplicationContext(),
                 "Group Clicked " + mHousingDiscussionList.get(groupPosition),
                 Toast.LENGTH_SHORT).show();*/
                //parent.expandGroup(groupPosition);
                //Constants.QUESTION_GROUP_CLICKED = groupPosition;
             //   Constants.QUESTION_ID_GROUP_CLICKED = mHousingDiscussionList.get(groupPosition).getDiscussionItemID();
                return true;
            }
        });

        // Listview on child click listener
        mDiscussionExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        mHousingDiscussionList.get(groupPosition)
                                + " : "
                                + mHousingAnswerList.get(
                                mHousingDiscussionList.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // Listview Group expanded listener
        mDiscussionExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        mHousingDiscussionList.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        mDiscussionExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        mHousingDiscussionList.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    /*
 * Preparing the list data
 */
    private void prepareListData() {
/*        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();*/

/*        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");*/

        /*for(int i=0; i<mHousingDiscussionList.size(); i++){
              //mHousingAnswerList.put(mHousingDiscussionList.get(i), top250); // Header, Child data
            //mHousingAnswerList.put(questionID, answers);
            getAnswersFromNetwork(mHousingDiscussionList.get(i).getDiscussionItemID());
        }*/
      /*  // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        for(int i=0; i<mHousingDiscussionList.size(); i++){
          //  mHousingAnswerList.put(mHousingDiscussionList.get(i), top250); // Header, Child data
        }*/

/*        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }

    public void postQuestionVolley(Context context){
        mQuestionToPost = mQuestionEditText.getText().toString();
        /*ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();*/

        mPostCreateAccountListener.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Constants.POST_QUESTION_URL,
                new Response.Listener<String>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());
                        mPostCreateAccountListener.requestCompleted();

                        /*JSONObject json = jsonParser.makeHttpRequest( Constants.REGISTRATION_URL, "POST", params);
                        // checking log for json response
                        Log.d("Registration attempt", json.toString());
                        // success tag for json
                        success = json.getInt(Constants.TAG_SUCCESS);*/
                        /*try {
                            // success tag for json
                            success = response.getInt(Constants.TAG_SUCCESS);
                            message = response.getString(Constants.TAG_MESSAGE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        //responseCode = json.getStatusLine().getStatusCode();
                        if (success == 1) {
                            mPostCreateAccountListener.requestCompleted();

                            /*Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
                            // this finish() method is used to tell android os that we are done with current
                            // activity now! Moving to other activity
                            finish();
                            startActivity(intent);*/
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mPostCreateAccountListener.requestEndedWithError(error);
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("questionUserID", mUserID);
                params.put("question", mQuestionToPost);
                params.put("category", "Housing");

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
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
        queue.add(jsonObjReq);
    }

    public void postQuestion(){
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.POST_QUESTION_URL, params,
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

    @Override
    public void onClick(View v) {
        if (Util.isNetworkAvailable(getApplicationContext())) {
            if (v.getId() == R.id.question_post_button) {
               // postQuestion();
               // postQuestionVolley(this);
                /*final Dialog ratingDialog = new Dialog(this, R.style.FullHeightDialog);
                ratingDialog.setContentView(R.layout.rating_dialog);
                ratingDialog.setCancelable(true);
                ratingDialog.show();

                Button submitButton = (Button) ratingDialog.findViewById(R.id.dialogRatingSubmitButton);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RatingBar ratingBar = (RatingBar) ratingDialog.findViewById(R.id.dialogRatingBar);
                        float rating = ratingBar.getRating();
                        postInstructorRating(rating);
                        ratingDialog.dismiss();
                    }
                });*/
            } /*else if(v.getId() == R.id.discussion_board_write_answer_button){ //write answer button
                final Dialog writeAnswerDialog = new Dialog(this, R.style.FullHeightDialog);
                writeAnswerDialog.setContentView(R.layout.write_answer_dialog);
                writeAnswerDialog.setCancelable(true);
                writeAnswerDialog.show();

                Button submitButton = (Button) writeAnswerDialog.findViewById(R.id.dialog_answer_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) writeAnswerDialog.findViewById(R.id.edit_text_answer);
                        String answer = editText.getText().toString();
                        //postInstructorComment(comment);
                        writeAnswerDialog.dismiss();
                    }
                });
            }*/
        } else {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog();
            customAlertDialog.showAlertDialog(this, "Network Unavailable", "Please check network connection and try again.");
        }
    }
}
