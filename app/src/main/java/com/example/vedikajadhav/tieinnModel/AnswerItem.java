package com.example.vedikajadhav.tieinnModel;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Date;

/**
 * Created by Vedika Jadhav on 9/14/2015.
 */
public class AnswerItem {
    private int mAnswerItemID;
    private String mAnswerItemUserID;
    private int mQuestionID;
    private String mAnswerItemText;
    private int mAnswerRecommendCount = 0;
    private Button mAnswerItemRecommendButton;
    private String mAnswerItemDate;
    private Button mAnswerItemEditButton;

    public int getAnswerItemID() {
        return mAnswerItemID;
    }

    public void setAnswerItemID(int answerItemID) {
        mAnswerItemID = answerItemID;
    }

    public String getAnswerItemUserID() {
        return mAnswerItemUserID;
    }

    public void setAnswerItemUserID(String answerItemUserID) {
        mAnswerItemUserID = answerItemUserID;
    }

    public String getAnswerItemText() {
        return mAnswerItemText;
    }

    public void setAnswerItemText(String AnswerItemText) {
        mAnswerItemText = AnswerItemText;
    }

    public int getQuestionID() {
        return mQuestionID;
    }

    public void setQuestionID(int questionID) {
        mQuestionID = questionID;
    }

    public int getAnswerRecommendCount() {
        return mAnswerRecommendCount;
    }

    public void setAnswerRecommendCount(int recommendCount) {
        mAnswerRecommendCount = recommendCount;
    }

    public Button getAnswerItemRecommendButton() {
        return mAnswerItemRecommendButton;
    }

    public void setAnswerItemRecommendButton(Button AnswerItemAnswerButton) {
        mAnswerItemRecommendButton = AnswerItemAnswerButton;
    }

    public String getAnswerItemDate() {
        return mAnswerItemDate;
    }

    public void setAnswerItemDate(String answerItemDate) {
        mAnswerItemDate = answerItemDate;
    }
}
