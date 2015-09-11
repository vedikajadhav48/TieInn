package com.example.vedikajadhav.tieinn;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.vedikajadhav.tieinnLibrary.DiscussionListAdapter;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import java.util.ArrayList;


public class AcademicCategoryActivity extends ActionBarActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_category);

        for(int i=0; i<5; i++){
            //JSONObject firstPerson = (JSONObject) data.get(i);

            newDiscussionItem.setDiscussionItemText(getResources().getString(R.string.discussion_item_question));
            newDiscussionItem.setDiscussionCategory("Academic");
            //newInstructor.setFirstName(firstPerson.getString("firstName"));
            // newInstructor.setLastName(firstPerson.getString("lastName"));
            mHousingCategoryList.add(0,newDiscussionItem);
        }

       // question = getIntent().getStringExtra(Intent_question);
       // category = getIntent().getStringExtra(Intent_category);
       // newDiscussionItem.setDiscussionItemText(question);
        //newDiscussionItem.setDiscussionCategory("Academic");
       // mHousingCategoryList.add(0,newDiscussionItem);
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
        getMenuInflater().inflate(R.menu.menu_academic_category, menu);
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
