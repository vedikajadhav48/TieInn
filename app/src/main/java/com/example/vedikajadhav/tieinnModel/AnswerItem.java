package com.example.vedikajadhav.tieinnModel;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Vedika Jadhav on 9/14/2015.
 */
public class AnswerItem {
    private ImageView mAnswerItemImage;
    private String mAnswerItemText;
    private String mAnswerItemPostDate;
    private Button mAnswerItemRecommendButton;
    private Button mAnswerItemEditButton;

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

    public Button getAnswerItemRecommendButton() {
        return mAnswerItemRecommendButton;
    }

    public void setAnswerItemRecommendButton(Button AnswerItemAnswerButton) {
        mAnswerItemRecommendButton = AnswerItemAnswerButton;
    }
}
