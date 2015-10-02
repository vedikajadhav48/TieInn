package com.example.vedikajadhav.tieinnModel;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Vedika Jadhav on 9/14/2015.
 */
public class AnswerItem {
    private int mAnswerItemID;
    private ImageView mAnswerItemImage;
    private String mAnswerItemText;
    private String mAnswerItemPostDate;
    private int mQuestionID;
    private int mRecommendCount;
    private Button mAnswerItemRecommendButton;
    private Button mAnswerItemEditButton;

    public int getAnswerItemID() {
        return mAnswerItemID;
    }

    public void setAnswerItemID(int answerItemID) {
        mAnswerItemID = answerItemID;
    }

    public ImageView getAnswerItemImage() {
        return mAnswerItemImage;
    }

    public void setAnswerItemImage(ImageView AnswerItemImage) {
        mAnswerItemImage = AnswerItemImage;
    }

    public String getAnswerItemText() {
        return mAnswerItemText;
    }

    public void setAnswerItemText(String AnswerItemText) {
        mAnswerItemText = AnswerItemText;
    }

    public String getAnswerItemPostDate() {
        return mAnswerItemPostDate;
    }

    public void setAnswerItemPostDate(String AnswerItemPostDate) {
        mAnswerItemPostDate = AnswerItemPostDate;
    }

    public int getQuestionID() {
        return mQuestionID;
    }

    public void setQuestionID(int questionID) {
        mQuestionID = questionID;
    }

    public int getRecommendCount() {
        return mRecommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        mRecommendCount = recommendCount;
    }

    public Button getAnswerItemRecommendButton() {
        return mAnswerItemRecommendButton;
    }

    public void setAnswerItemRecommendButton(Button AnswerItemAnswerButton) {
        mAnswerItemRecommendButton = AnswerItemAnswerButton;
    }
}
