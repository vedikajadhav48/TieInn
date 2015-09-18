package com.example.vedikajadhav.tieinnLibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vedikajadhav.tieinn.R;
import com.example.vedikajadhav.tieinnModel.AnswerItem;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Vedika Jadhav on 9/5/2015.
 */
public class DiscussionListAdapter extends ArrayAdapter<DiscussionItem>{
    private static final String TAG= "DiscussionListAdapter";
    private final Activity mContext;
    private LayoutInflater mLayoutInflator;
    private final ArrayList<DiscussionItem> mDiscussionItemList;
    private Button answerButton = null;
    private ButtonClickListener mButtonClickListener;
    AnswerListAdapter answerListAdapter;
    private ArrayList<AnswerItem> mHousingAnswerList = new ArrayList<>();

    public DiscussionListAdapter(ArrayList<DiscussionItem> discussionItemList, Activity context) {
        super(context, 0, discussionItemList);
        this.mContext = context;
       // mLayoutInflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDiscussionItemList = discussionItemList;
       // mButtonClickListener = buttonClickListener;
    }

/*    @Override
    public int getCount() {
        return 0;
    }*/

/*    @Override
    public Object getItem(int position) {
        return null;
    }*/

 /*   @Override
    public long getItemId(int position) {
        return 0;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        YourWrapper wrapper = null;

        if(convertView==null) {
            convertView = (mContext).getLayoutInflater().inflate(R.layout.discussion_board_list_row, null, true);
        }
        /*if(row==null){
            //convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.discussion_board_list_row, null);
            row = ((Activity)mContext).getLayoutInflater().inflate(R.layout.discussion_board_list_row, null);
            wrapper = new YourWrapper(row);
            row.setTag(wrapper);
        }
        else{
            wrapper = (YourWrapper)row.getTag();
        }*/


       // ImageView thumbImage=(ImageView)row.findViewById(R.id.discussion_board_question_image);
        TextView categoryTextView = (TextView)convertView.findViewById(R.id.discussion_board_question_category);
        TextView questionTextView=(TextView)convertView.findViewById(R.id.discussion_board_question_text);
        answerButton=(Button)convertView.findViewById(R.id.discussion_board_write_answer_button);

        DiscussionItem discussionItem = getItem(position);
        categoryTextView.setText(discussionItem.getDiscussionCategory());
        questionTextView.setText(discussionItem.getDiscussionItemText());
        answerButton.setTag(position);//For passing the list item index
        answerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                /*if(mButtonClickListener != null){
                    mButtonClickListener.onButtonClick((Integer)v.getTag());
                }*/
                final Dialog writeAnswerDialog = new Dialog(mContext, R.style.FullHeightDialog);
                writeAnswerDialog.setContentView(R.layout.write_answer_dialog);
                writeAnswerDialog.setCancelable(true);
                writeAnswerDialog.show();

                Button submitButton = (Button) writeAnswerDialog.findViewById(R.id.dialog_answer_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) writeAnswerDialog.findViewById(R.id.edit_text_answer);
                        String answer = editText.getText().toString();
                        //postInstructorComment(comment);
                        writeAnswerDialog.dismiss();
                    }
                });
            }
        });

       /* wrapper.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog writeAnswerDialog = new Dialog(getContext(), R.style.FullHeightDialog);
                writeAnswerDialog.setContentView(R.layout.write_answer_dialog);
                writeAnswerDialog.setCancelable(true);
                writeAnswerDialog.show();

                Button submitButton = (Button) writeAnswerDialog.findViewById(R.id.dialog_answer_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) writeAnswerDialog.findViewById(R.id.edit_text_answer);
                        String answer = editText.getText().toString();
                        //postInstructorComment(comment);
                        writeAnswerDialog.dismiss();
                    }
                });
            }
        });*/
       /* for(int i=0; i<5; i++){
            AnswerItem newAnswerItem = new AnswerItem();
            newAnswerItem.setAnswerItemText("Starbucks is the favourite coffee place");
            mHousingAnswerList.add(0, newAnswerItem);
        }*/
       // answerListAdapter = new AnswerListAdapter(mHousingAnswerList, getContext());
        //answerListView.setAdapter(answerListAdapter);
        return convertView;
    }
}
