package com.example.vedikajadhav.tieinnModel;

import android.media.Image;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static android.view.ViewDebug.trace;

/**
 * Created by Vedika Jadhav on 9/5/2015.
 */
public class QuestionItem {
    private static final String TAG = "QuestionItem";
    private int mQuestionItemID;
    private String mQuestionItemUserID;
    private String mQuestionItemText;
    private String mQuestionItemCategory;
    private Button mQuestionItemWriteAnswerButton;
    private Button mQuestionItemEditButton;
    private Date mQuestionItemDate;
    //private ListView mQuestionItemAnswerList;

    public int getQuestionItemID() {
        return mQuestionItemID;
    }

    public void setQuestionItemID(int QuestionItemID) {
        mQuestionItemID = QuestionItemID;
    }

    public String getQuestionItemUserID() {
        return mQuestionItemUserID;
    }

    public void setQuestionItemUserID(String QuestionUserID) {
        mQuestionItemUserID = QuestionUserID;
    }

    public String getQuestionItemText() {
        return mQuestionItemText;
    }

    public void setQuestionItemText(String QuestionItemText) {
        mQuestionItemText = QuestionItemText;
    }

    public String getQuestionItemCategory() {
        return mQuestionItemCategory;
    }

    public void setQuestionItemCategory(String QuestionItemCategory) {
        mQuestionItemCategory = QuestionItemCategory;
    }

    public Button getQuestionItemWriteAnswerButton() {
        return mQuestionItemWriteAnswerButton;
    }

    public void setQuestionItemWriteAnswerButton(Button QuestionItemAnswerButton) {
        mQuestionItemWriteAnswerButton = QuestionItemAnswerButton;
    }

    public Button getQuestionItemEditButton() {
        return mQuestionItemEditButton;
    }

    public void setQuestionItemEditButton(Button questionItemEditButton) {
        mQuestionItemEditButton = questionItemEditButton;
    }

    public Date getQuestionItemDate() {
        return mQuestionItemDate;
    }

    public void setQuestionItemDate(Date questionItemDate) {
        mQuestionItemDate = questionItemDate;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("questionID", mQuestionItemID);
            jsonObject.put("questionUserID", mQuestionItemUserID);
            jsonObject.put("questionText", mQuestionItemText);
            jsonObject.put("questionCategory", mQuestionItemCategory);
        } catch (JSONException e) {
            Log.i("DefaultListItem.toString JSONException: ", e.getMessage());
        }
        return jsonObject;
    }
}
