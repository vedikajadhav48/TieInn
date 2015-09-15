package com.example.vedikajadhav.tieinnLibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vedikajadhav.tieinn.R;
import com.example.vedikajadhav.tieinnModel.AnswerItem;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import java.util.ArrayList;

/**
 * Created by Vedika Jadhav on 9/14/2015.
 */
public class AnswerListAdapter extends ArrayAdapter<AnswerItem>{
    private Context mContext;

    public AnswerListAdapter(ArrayList<AnswerItem> answerItemList, Context context) {
        super(context.getApplicationContext(), 0, answerItemList);
        mContext = context;
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
        if(convertView==null){
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.answer_list_row, null);
        }

        AnswerItem answerItem = getItem(position);
        ImageView thumbImage=(ImageView)convertView.findViewById(R.id.answer_thumbnail_image);
       // TextView categoryTextView = (TextView)convertView.findViewById(R.id.discussionBoardQuestionCategory);
        TextView answerTextView=(TextView)convertView.findViewById(R.id.answerText);
        Button recommendAnswerButton=(Button)convertView.findViewById(R.id.recommendAnswerButton);
        Button editAnswerButton=(Button)convertView.findViewById(R.id.editAnswerButton);
        answerTextView.setText(answerItem.getAnswerItemText());
        return convertView;
    }
}
