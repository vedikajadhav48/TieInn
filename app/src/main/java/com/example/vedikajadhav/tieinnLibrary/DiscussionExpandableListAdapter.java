package com.example.vedikajadhav.tieinnLibrary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.vedikajadhav.tieinn.R;
import com.example.vedikajadhav.tieinnModel.AnswerItem;
import com.example.vedikajadhav.tieinnModel.Constants;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;
import com.facebook.share.widget.LikeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vedika Jadhav on 9/19/2015.
 */
public class DiscussionExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "DiscussionExpandableListAdapter";
    private Context mContext;
    private List<DiscussionItem> mListDataHeader;
    private HashMap<Integer, List<AnswerItem>> mListDataChild;
    private String answer;
    private static PostCreateAccountResponseListener mPostCreateAccountListener;
    private String mUserID;
    private int mQuestionID;
    SessionManager mSession;

    public DiscussionExpandableListAdapter(Context context, List<DiscussionItem> listDataHeader,
                                       HashMap<Integer, List<AnswerItem>> listChildData, String userID) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        mUserID = userID;
        mPostCreateAccountListener = new PostCreateAccountResponseListener() {
            @Override
            public void requestStarted() {

            }

            @Override
            public void requestCompleted() {

            }

            @Override
            public void requestEndedWithError(VolleyError error) {

            }
        };
    }
    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i(TAG,"groupPOsition" + groupPosition);
        Log.i(TAG,"this.mListDataHeader" + this.mListDataHeader);
        Log.i(TAG,"size" + this.mListDataChild.get(3));
        Log.i(TAG,"size" + this.mListDataChild.get(3).size());
        //return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        return (this.mListDataChild.get(this.mListDataHeader.get(groupPosition).getDiscussionItemID())).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition).getDiscussionItemID())
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        mQuestionID = mListDataHeader.get(groupPosition).getDiscussionItemID();
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DiscussionItem headerTitle = (DiscussionItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.discussion_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.discussion_board_question_text);
        Button writeAnswerButton=(Button)convertView.findViewById(R.id.discussion_board_write_answer_button);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getDiscussionItemText());

        writeAnswerButton.setTag(groupPosition);//For passing the list item index
        writeAnswerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final Dialog writeAnswerDialog = new Dialog(mContext, R.style.FullHeightDialog);
                writeAnswerDialog.setContentView(R.layout.write_answer_dialog);
                writeAnswerDialog.setCancelable(true);
                writeAnswerDialog.show();

                Button submitButton = (Button) writeAnswerDialog.findViewById(R.id.dialog_answer_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) writeAnswerDialog.findViewById(R.id.edit_text_answer);
                        answer = editText.getText().toString();
                        postAnswerVolley(mContext, groupPosition);
                        writeAnswerDialog.dismiss();
                    }
                });
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Log.i(TAG, "getChild returns" + getChild(groupPosition, childPosition));
        final AnswerItem childItem = (AnswerItem)getChild(groupPosition, childPosition);
        final String childText = childItem.getAnswerItemText();
        final int recommendCount = childItem.getRecommendCount();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.discussion_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.discussion_board_answer_text);
        final Button recommendAnswerButton=(Button)convertView.findViewById(R.id.discussion_board_recommend_answer);
        final EditText recommendAnswerEditText=(EditText)convertView.findViewById(R.id.dicussion_board_recommend_count);

        txtListChild.setText(childText);
        recommendAnswerButton.setTag(recommendCount);//For passing the list item index
        recommendAnswerEditText.setText(String.valueOf(recommendCount));
        recommendAnswerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //increment recommend counter
                final int recommendCount = childItem.getRecommendCount();
                int count = recommendCount;
                updateRecommendationOnNetwork(childItem, ++count);
                recommendAnswerEditText.setText(String.valueOf(count));
            }
        });
        return convertView;
    }

    public void updateRecommendationOnNetwork(final AnswerItem childItem, final int count){
        Log.i(TAG, "Network Request for questions");
        // get user data from session
        mSession = SessionManager.getInstance(mContext);
        HashMap<String, String> user = mSession.getUserDetails();
        mUserID = user.get(SessionManager.KEY_USERID);
        String url = Constants.UPDATE_RECOMMENDATIONS_URL + "userID=" + mUserID + "&answerID=" + childItem.getAnswerItemID() + "&numberOfRecommendation=" + count;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            int success;
            String message;
            @Override
            public void onResponse(JSONObject response) {
                try {
                    success = response.getInt(Constants.TAG_SUCCESS);
                    message = response.getString(Constants.TAG_MESSAGE);

                    if (success == 1) {
                        Log.d("Recommendation updated", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error Response");
            }
        });

        //NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void postAnswerVolley(Context context, final int groupPosition){
       // mQuestionToPost = mQuestionEditText.getText().toString();
        /*ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();*/

        mPostCreateAccountListener.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Constants.POST_ANSWER_URL,
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
                params.put("answerUserID", mUserID);
                params.put("questionID", String.valueOf(mListDataHeader.get(groupPosition).getDiscussionItemID()));
                params.put("answer", answer);

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
        queue.add(jsonObjReq);
    }

}
