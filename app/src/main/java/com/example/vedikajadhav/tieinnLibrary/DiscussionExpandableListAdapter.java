package com.example.vedikajadhav.tieinnLibrary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.example.vedikajadhav.tieinnModel.QuestionItem;

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
    private List<QuestionItem> mListDataHeader;
    private HashMap<Integer, List<AnswerItem>> mListDataChild;
    SessionManager mSession;
    private String mUserID;
    private String answer;
    private int mQuestionID;
    private int mAnswerID;

    public DiscussionExpandableListAdapter(Context context, List<QuestionItem> listDataHeader,
                                       HashMap<Integer, List<AnswerItem>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        // get user data from session
        mSession = SessionManager.getInstance(mContext);
        HashMap<String, String> user = mSession.getUserDetails();
        this.mUserID = user.get(SessionManager.KEY_USERID);
    }
    @Override
    public int getGroupCount() {
        Log.i(TAG,"getGroupCount" + this.mListDataHeader.size());
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i(TAG,"groupPOsition" + groupPosition);
        Log.i(TAG,"getGroup(groupPosition).getQuestionItemID()" + getGroup(groupPosition).getQuestionItemID());
        Log.i(TAG,"arrayList" + this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID()));
        Log.i(TAG,"size" + this.mListDataChild.get(3).size());
        //return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        //return (this.mListDataChild.get(this.mListDataHeader.get(groupPosition).getQuestionItemID())).size();
        return (this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID())).size();
    }

    @Override
    public QuestionItem getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public AnswerItem getChild(int groupPosition, int childPosition) {
        Log.i(TAG,"groupPOsition" + groupPosition);
        Log.i(TAG,"childPosition" + childPosition);
        Log.i(TAG,"DataHEader discussionID" + this.mListDataHeader.get(groupPosition).getQuestionItemID());
        Log.i(TAG,"groupPOsition" + this.mListDataChild.get(this.mListDataHeader.get(groupPosition).getQuestionItemID()).get(childPosition));
       // return this.mListDataChild.get(this.mListDataHeader.get(groupPosition).getQuestionItemID()).get(childPosition);
        return (this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID())).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        mQuestionID = getGroup(groupPosition).getQuestionItemID();
        return mQuestionID;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        mAnswerID = getChild(groupPosition, childPosition).getAnswerItemID();
        return mAnswerID;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        QuestionItem questionGroupItem = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.discussion_list_group, null);
        }

        TextView questionTextView = (TextView) convertView
                .findViewById(R.id.discussion_board_question_text);
        Button writeAnswerButton=(Button)convertView.findViewById(R.id.discussion_board_write_answer_button);

        questionTextView.setTypeface(null, Typeface.BOLD);
        questionTextView.setText(questionGroupItem.getQuestionItemText());

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
        final AnswerItem AnswerChildItem = getChild(groupPosition, childPosition);
        final String childText = AnswerChildItem.getAnswerItemText();
        final int recommendCount = AnswerChildItem.getAnswerRecommendCount();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.discussion_list_item, null);

            final TextView answerTextView = (TextView) convertView.findViewById(R.id.discussion_board_answer_text);
            final Button recommendAnswerButton = (Button) convertView.findViewById(R.id.discussion_board_recommend_answer);
            final EditText recommendAnswerCountEditText = (EditText) convertView.findViewById(R.id.dicussion_board_recommend_count);

            answerTextView.setText(childText);
            recommendAnswerCountEditText.setText(String.valueOf(AnswerChildItem.getAnswerRecommendCount()));

            recommendAnswerCountEditText.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // AnswerItem element=(AnswerItem)recommendAnswerEditText.getTag();

                    //childItem.setAnswerRecommendCount(Integer.parseInt(s.toString()));
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //recommendAnswerEditText.setText(String.valueOf(recommendCount));
                }
                public void afterTextChanged(Editable s) {
                    //childItem.setRecommendCount(Integer.parseInt(s.toString()));
                    //childItem.setRecommendCount(Integer.parseInt(recommendAnswerEditText.getText().toString()));
                }
            });

            recommendAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //increment recommend counter
                    // final int recommendCount = childItem.getRecommendCount();
                    int count = AnswerChildItem.getAnswerRecommendCount();
                    updateRecommendationOnNetwork(recommendAnswerCountEditText, AnswerChildItem, ++count);
                    // recommendAnswerEditText.setText(String.valueOf(count));
                }
            });
        }
        return convertView;
    }

    public void updateRecommendationOnNetwork(final EditText recommendAnswerEditText, final AnswerItem childItem, final int count){
        Log.i(TAG, "Network Request for questions");
        String url = Constants.UPDATE_RECOMMENDATIONS_URL + "userID=" + mUserID + "&answerID=" + childItem.getAnswerItemID() + "&numberOfRecommendations=" + count;
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
                        recommendAnswerEditText.setText(String.valueOf(count));
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void postAnswerVolley(final Context context, final int groupPosition){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.POST_ANSWER_URL,
                new Response.Listener<String>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObjectResponse = new JSONObject(response);
                            success = jsonObjectResponse.getInt("success");
                            message = jsonObjectResponse.getString("message");

                            if (success == 1) {
                                /*AnswerItem newAnswerItem = new AnswerItem();
                                newAnswerItem.setAnswerItemID(Integer.parseInt(message));
                                newAnswerItem.setAnswerUserID(userID);
                                newAnswerItem.setQuestionID(mQuestionID);
                                newAnswerItem.setAnswerItemText(questionToPost);
                                mAnswerList.add(0, newAnswerItem);
                                mDiscussionExpandableListAdapter.notifyDataSetChanged();*/
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Error Occurred", "Answer could not be posted!!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("answerUserID", mUserID);
                params.put("questionID", String.valueOf(mListDataHeader.get(groupPosition).getQuestionItemID()));
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
        queue.add(stringRequest);
    }

}
