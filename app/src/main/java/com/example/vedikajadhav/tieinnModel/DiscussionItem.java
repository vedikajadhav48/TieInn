package com.example.vedikajadhav.tieinnModel;

import android.media.Image;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Vedika Jadhav on 9/5/2015.
 */
public class DiscussionItem {
    private int mDiscussionItemID;
    private ImageView mDiscussionItemImage;
    private String mDiscussionCategory;
    private String mDiscussionItemText;
    private String mDiscussionItemPostDate;
    private Button mDiscussionItemAnswerButton;
    private ListView mDiscussionItemAnswerList;

    public int getDiscussionItemID() {
        return mDiscussionItemID;
    }

    public void setDiscussionItemID(int discussionItemID) {
        mDiscussionItemID = discussionItemID;
    }

    public ImageView getDiscussionItemImage() {
        return mDiscussionItemImage;
    }

    public void setDiscussionItemImage(ImageView discussionItemImage) {
        mDiscussionItemImage = discussionItemImage;
    }

    public String getDiscussionCategory() {
        return mDiscussionCategory;
    }

    public void setDiscussionCategory(String discussionCategory) {
        mDiscussionCategory = discussionCategory;
    }

    public Button getDiscussionItemAnswerButton() {
        return mDiscussionItemAnswerButton;
    }

    public void setDiscussionItemAnswerButton(Button discussionItemAnswerButton) {
        mDiscussionItemAnswerButton = discussionItemAnswerButton;
    }

    public String getDiscussionItemText() {
        return mDiscussionItemText;
    }

    public void setDiscussionItemText(String discussionItemText) {
        mDiscussionItemText = discussionItemText;
    }

    public String getDiscussionItemPostDate() {
        return mDiscussionItemPostDate;
    }

    public void setDiscussionItemPostDate(String discussionItemPostDate) {
        mDiscussionItemPostDate = discussionItemPostDate;
    }
}
