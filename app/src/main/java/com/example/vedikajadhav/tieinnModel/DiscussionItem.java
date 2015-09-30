package com.example.vedikajadhav.tieinnModel;

import android.media.Image;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.ViewDebug.trace;

/**
 * Created by Vedika Jadhav on 9/5/2015.
 */
public class DiscussionItem {
    private static final String TAG = "DiscussionItem";
    private int mDiscussionItemID;
    private ImageView mDiscussionItemImage;
    private String mDiscussionItemCategory;
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
        return mDiscussionItemCategory;
    }

    public void setDiscussionCategory(String discussionItemCategory) {
        mDiscussionItemCategory = discussionItemCategory;
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

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("questionID", mDiscussionItemID);
            jsonObject.put("questionText", mDiscussionItemText);
            jsonObject.put("questionCategory", mDiscussionItemCategory);
            //jsonObject.put("questionText", _category);
        } catch (JSONException e) {
            Log.i("DefaultListItem.toString JSONException: ", e.getMessage());
        }
        return jsonObject;
    }
}
