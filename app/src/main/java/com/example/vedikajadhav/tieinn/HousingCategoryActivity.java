package com.example.vedikajadhav.tieinn;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.vedikajadhav.tieinnLibrary.DiscussionListAdapter;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HousingCategoryActivity extends ActionBarActivity {

    private EditText questionEditText;
    private Button questionPostButton;
    ListView discussionListView;
    DiscussionListAdapter discussionListAdapter;
    private ArrayList<DiscussionItem> mHousingCategoryList = new ArrayList<>();
    DiscussionItem newDiscussionItem = new DiscussionItem();
    private String questionToPost;
    public static final String Intent_question = "com.example.vedikajadhav.tieinn.Intent_question";
    public static final String Intent_category = "com.example.vedikajadhav.tieinn.Intent_category";
    private String question;
    private String category;
    private JSONArray questionsJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housing_category);

      /*  for(int i=0; i<5; i++){
            //JSONObject firstPerson = (JSONObject) data.get(i);

            newDiscussionItem.setDiscussionItemText(getResources().getString(R.string.discussion_item_question));
            //newInstructor.setFirstName(firstPerson.getString("firstName"));
            // newInstructor.setLastName(firstPerson.getString("lastName"));
            mHousingCategoryList.add(0,newDiscussionItem);
        }*/

        //question = getIntent().getStringExtra(Intent_question);
        question = getIntent().getStringExtra(Intent_question);
        category = getIntent().getStringExtra(Intent_category);
        try {
            questionsJSONArray = new JSONArray(question);
            for(int i=0; i<questionsJSONArray.length(); i++){
                JSONObject firstQuestion = (JSONObject) questionsJSONArray.get(i);
                DiscussionItem newDiscussionItem = new DiscussionItem();
                newDiscussionItem.setDiscussionItemText(firstQuestion.getString("Question"));
                newDiscussionItem.setDiscussionCategory(firstQuestion.getString("Category"));
               // newInstructor.setLastName(firstPerson.getString("lastName"));
                mHousingCategoryList.add(0, newDiscussionItem);
            }
            //instructorAdapter.notifyDataSetChanged();
           // Log.i(TAG, "getInstructorList" + instructorAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questionEditText = (EditText)findViewById(R.id.question_edit_text);
        questionPostButton = (Button)findViewById(R.id.question_post_button);
        discussionListView = (ListView)findViewById(R.id.discussionBoardItemList);
        discussionListAdapter = new DiscussionListAdapter(mHousingCategoryList, this);
        discussionListView.setAdapter(discussionListAdapter);

    }

    public void postQuestion(View postQuestionButton){
        questionToPost = questionEditText.getText().toString();
        DiscussionItem newDiscussionItem = new DiscussionItem();
        newDiscussionItem.setDiscussionItemText(questionToPost);
        mHousingCategoryList.add(0, newDiscussionItem);
        discussionListAdapter.notifyDataSetChanged();
        /*discussionListView.post(new Runnable() {
            @Override
            public void run() {
                discussionListView.smoothScrollToPosition(0);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_housing_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
