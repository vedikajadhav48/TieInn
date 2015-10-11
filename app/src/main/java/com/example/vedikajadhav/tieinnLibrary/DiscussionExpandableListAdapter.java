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
import android.widget.ImageView;
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
public class DiscussionExpandableListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private static final String TAG = "DiscussionExpandableListAdapter";
    private Context mContext;
    private List<QuestionItem> mListDataHeader;
    private HashMap<Integer, List<AnswerItem>> mListDataChild;
    SessionManager mSession;
    private String mUserID;
    private String mAnswerToPost;
    private String mQuestionToUpdate;
    private String mAnswerToUpdate;
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
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return (this.mListDataChild.get((int)getGroupId(groupPosition))).size();
        Log.i(TAG,"mListDataHeader" + mListDataHeader.toString());
        Log.i(TAG,"mListDataChild" + mListDataChild.toString());
        Log.i(TAG, "groupPosition" + String.valueOf(groupPosition));
        Log.i(TAG, "getGroup(groupPosition).getQuestionItemID()" + String.valueOf(getGroup(groupPosition).getQuestionItemID()));
        Log.i(TAG,"getGroup answerList" +this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID()));
//        Log.i(TAG, "size" + this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID()).size());
       // return (this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID())).size();

        if((this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID()))==null){
            return 0;
        }else {
            return (this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID())).size();
        }
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
    public int getChildType(int groupPosition, int childPosition) {
        return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public int getGroupType(int groupPosition) {
        return super.getGroupType(groupPosition);
    }

    @Override
    public int getChildTypeCount() {
        return super.getChildTypeCount();
    }

    @Override
    public int getGroupTypeCount() {
        return super.getGroupTypeCount();
    }

    @Override
    public QuestionItem getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public AnswerItem getChild(int groupPosition, int childPosition) {
       // return (this.mListDataChild.get((int)getGroupId(groupPosition))).get(childPosition);
        //Log.i(TAG, "childList" + this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID()));
        //Log.i(TAG, "getChild" + (this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID())).get(childPosition));
        /*if(this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID()) == null){
            return null;
        }else{*/
            return (this.mListDataChild.get(getGroup(groupPosition).getQuestionItemID())).get(childPosition);
        //}
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        /*if (getChildrenCount(groupPosition) > 0) {
            parent.findViewById(R.id.discussion_expandable_list_view).setVisibility(parent.INVISIBLE);
            ImageView inidicatorImage = (ImageView) convertView.findViewById(R.id.explist_indicator);
            //indicator.setVisibility(View.VISIBLE);
            *//*viewHolder.indicator.setImageResource(
                    isExpanded ? R.drawable.indicator_expanded : R.drawable.indicator);*//*
        } else {
            //viewHolder.indicator.setVisibility(View.GONE);
        }*/
        QuestionItem questionGroupItem = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.discussion_list_group, null);
        }

        Button editQuestionButton = (Button)convertView.findViewById(R.id.discussion_board_edit_question_button);
        if(!questionGroupItem.getQuestionItemUserID().equalsIgnoreCase(mUserID)){
            editQuestionButton.setVisibility(parent.INVISIBLE);
        }else{
            editQuestionButton.setVisibility(parent.VISIBLE);
            editQuestionButton.setTag(questionGroupItem);
            editQuestionButton.setOnClickListener(this);
        }
        TextView questionTextView = (TextView) convertView
                .findViewById(R.id.discussion_board_question_text);
        TextView questionDateTextView = (TextView) convertView.findViewById(R.id.dicussion_board_question_post_date);
        Button writeAnswerButton=(Button)convertView.findViewById(R.id.discussion_board_write_answer_button);

        questionTextView.setTypeface(null, Typeface.BOLD);
        questionTextView.setText(questionGroupItem.getQuestionItemText());
        questionDateTextView.setText("Posted On:" + questionGroupItem.getQuestionItemDate());

        writeAnswerButton.setTag(String.valueOf(groupPosition));//For passing the list item index
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
                        EditText answerEditText = (EditText) writeAnswerDialog.findViewById(R.id.edit_text_answer);
                        mAnswerToPost = answerEditText.getText().toString();

                        if (!mAnswerToPost.equalsIgnoreCase("")) {
                            answerEditText.setText("");
                            int questionID = (int) getGroupId(groupPosition);
                            postAnswerVolley(mContext, questionID);
                            //postQuestionToNetwork(this, mQuestionToPost, mUserID, mCategory);
                        } else {
                            CustomAlertDialog.showAlertDialog(mContext, "Empty Answer", "Enter an answer!!");
                        }
                        writeAnswerDialog.dismiss();
                    }
                });
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       /* if(getChild(groupPosition, childPosition) == null){
            return convertView;
        }
        else {*/
            final AnswerItem answerChildItem = getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.discussion_list_item, null);
            }

        Button editAnswerButton = (Button) convertView.findViewById(R.id.discussion_board_edit_answer_button);
        if(!answerChildItem.getAnswerItemUserID().equalsIgnoreCase(mUserID)){
            editAnswerButton.setVisibility(parent.INVISIBLE);
        }else{
            editAnswerButton.setVisibility(parent.VISIBLE);
            editAnswerButton.setTag(answerChildItem);
            editAnswerButton.setOnClickListener(this);
        }

            TextView answerTextView = (TextView) convertView.findViewById(R.id.discussion_board_answer_text);
            TextView answerDateTextView = (TextView) convertView.findViewById(R.id.dicussion_board_answer_post_date);
            final TextView recommendAnswerCountTextView = (TextView) convertView.findViewById(R.id.dicussion_board_recommend_count);
            Button recommendAnswerButton = (Button) convertView.findViewById(R.id.discussion_board_recommend_answer);

            answerTextView.setText(answerChildItem.getAnswerItemText());
            answerDateTextView.setText("Posted On:" + answerChildItem.getAnswerItemDate());
            recommendAnswerCountTextView.setText(String.valueOf(answerChildItem.getAnswerRecommendCount()) + " Likes");
            recommendAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //increment recommend counter
                    int count = answerChildItem.getAnswerRecommendCount();
                    updateRecommendationOnNetwork(recommendAnswerCountTextView, answerChildItem, ++count);
                }
            });

            return convertView;
       // }
    }

    public void updateRecommendationOnNetwork(final TextView recommendAnswerEditText, final AnswerItem childItem, final int count){
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
                        recommendAnswerEditText.setText(String.valueOf(count) + " Likes");
                        childItem.setAnswerRecommendCount(count);
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

    public void postAnswerVolley(final Context context, final int questionID){
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
                                JSONObject answerJSONObject = new JSONObject(message);
                                AnswerItem newAnswerItem = new AnswerItem();
                               // newAnswerItem.setAnswerItemID(Integer.parseInt(message));
                                newAnswerItem.setAnswerItemID(answerJSONObject.getInt("AnswerID"));
                                newAnswerItem.setAnswerItemUserID(mUserID);
                                newAnswerItem.setQuestionID(questionID);
                                newAnswerItem.setAnswerItemText(mAnswerToPost);
                                newAnswerItem.setAnswerItemDate(answerJSONObject.getString("AnswerDate"));
                                /*List<AnswerItem> answers = mListDataChild.get((int)getGroupId(groupPosition));
                                answers.add(newAnswerItem);*/
                                mListDataChild.get(questionID).add(newAnswerItem);
                                notifyDataSetChanged();
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
                params.put("questionID", String.valueOf(questionID));
                params.put("answer", mAnswerToPost);
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
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.discussion_board_edit_question_button){
            final Dialog editQuestionDialog = new Dialog(mContext, R.style.FullHeightDialog);
            editQuestionDialog.setContentView(R.layout.edit_dialog);
            editQuestionDialog.setCancelable(true);
            editQuestionDialog.show();

            //final int groupPosition = (int) v.getTag();
            //final QuestionItem questionGroupItem = getGroup(groupPosition);
            final QuestionItem questionGroupItem = (QuestionItem) v.getTag();
            final EditText questionEditText = (EditText) editQuestionDialog.findViewById(R.id.question_answer_edit_text);
            questionEditText.setText(questionGroupItem.getQuestionItemText());

            Button doneButton = (Button) editQuestionDialog.findViewById(R.id.question_answer_edit_done_button);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestionToUpdate = questionEditText.getText().toString();

                    if(!mQuestionToUpdate.equalsIgnoreCase("")){
                        questionEditText.setText("");
                        updateQuestionVolley(mContext, questionGroupItem);
                    }else{
                        CustomAlertDialog.showAlertDialog(mContext, "Empty Question", "Enter a Question!!");
                    }
                    editQuestionDialog.dismiss();
                }
            });
        }else if(v.getId() == R.id.discussion_board_edit_answer_button){//edit answer button
            final Dialog editAnswerDialog = new Dialog(mContext, R.style.FullHeightDialog);
            editAnswerDialog.setContentView(R.layout.edit_dialog);
            editAnswerDialog.setCancelable(true);
            editAnswerDialog.show();

            final AnswerItem answerChildItem = (AnswerItem) v.getTag();
            final EditText answerEditText = (EditText) editAnswerDialog.findViewById(R.id.question_answer_edit_text);
            answerEditText.setText(answerChildItem.getAnswerItemText());

            Button doneButton = (Button) editAnswerDialog.findViewById(R.id.question_answer_edit_done_button);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAnswerToUpdate = answerEditText.getText().toString();

                    if(!mAnswerToUpdate.equalsIgnoreCase("")){
                        answerEditText.setText("");
                        updateAnswerVolley(mContext, answerChildItem);
                    }else{
                        CustomAlertDialog.showAlertDialog(mContext, "Empty Answer", "Enter an Answer!!");
                    }
                    editAnswerDialog.dismiss();
                }
            });
        }
    }

    public void updateQuestionVolley(final Context context, final QuestionItem questionGroupItem){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_QUESTION_URL,
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
                                questionGroupItem.setQuestionItemText(mQuestionToUpdate);
                                notifyDataSetChanged();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Error Occurred", "Question could not be updated!!");
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
                params.put("question", mQuestionToUpdate);
                params.put("questionID", String.valueOf(questionGroupItem.getQuestionItemID()));

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

    public void updateAnswerVolley(final Context context, final AnswerItem answerChildItem){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_ANSWER_URL,
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
                                answerChildItem.setAnswerItemText(mAnswerToUpdate);
                                notifyDataSetChanged();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Error Occurred", "Answer could not be updated!!");
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
                params.put("answer", mAnswerToUpdate);
                params.put("answerID", String.valueOf(answerChildItem.getAnswerItemID()));

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
