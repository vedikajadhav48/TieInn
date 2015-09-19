package com.example.vedikajadhav.tieinnLibrary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vedikajadhav.tieinn.R;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vedika Jadhav on 9/19/2015.
 */
public class DiscussionExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<DiscussionItem> mListDataHeader;
    private HashMap<DiscussionItem, List<String>> mListDataChild;

    public DiscussionExpandableListAdapter(Context context, List<DiscussionItem> listDataHeader,
                                       HashMap<DiscussionItem, List<String>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }
    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DiscussionItem headerTitle = (DiscussionItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.discussion_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.discussion_board_question_text);
        Button answerButton=(Button)convertView.findViewById(R.id.discussion_board_write_answer_button);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getDiscussionItemText());

        answerButton.setTag(groupPosition);//For passing the list item index
        answerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
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

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.discussion_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.discussion_board_answer_text);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
