package com.example.vedikajadhav.tieinnLibrary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vedikajadhav.tieinn.R;
import com.example.vedikajadhav.tieinnModel.DiscussionItem;

import java.util.ArrayList;

/**
 * Created by Vedika Jadhav on 9/5/2015.
 */
public class DiscussionListAdapter extends ArrayAdapter<DiscussionItem> {

    //private Activity activity;
  //  private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
   // private static LayoutInflater inflater=null;
 //   public ImageLoader imageLoader;
    private Context mContext;

    public DiscussionListAdapter(ArrayList<DiscussionItem> discussionItemList, Context context) {
        super(context.getApplicationContext(), 0, discussionItemList);
        mContext = context;
       // data=d;
      //  inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // imageLoader=new ImageLoader(activity.getApplicationContext());
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
       // View vi=convertView;
        if(convertView==null){
            //convertView = inflater.inflate(R.layout.discussion_board_list_row, null);
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.discussion_board_list_row, null);
        }

        DiscussionItem discussionItem = getItem(position);
        ImageView thumbImage=(ImageView)convertView.findViewById(R.id.discussionBoardQuestionImage);
        TextView questionTextView=(TextView)convertView.findViewById(R.id.discussionBoardQuestionText);
        Button answerButton=(Button)convertView.findViewById(R.id.discussionBoardAnswerButton);

        //thumbImage.setImag;
        questionTextView.setText(discussionItem.getDiscussionItemText());
     /*   TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration*/
       /* ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        HashMap&lt;String, String&gt; song = new HashMap&lt;String, String&gt;();
        song = data.get(position);

        // Setting all values in listview
        title.setText(song.get(CustomizedListView.KEY_TITLE));
        artist.setText(song.get(CustomizedListView.KEY_ARTIST));
        duration.setText(song.get(CustomizedListView.KEY_DURATION));
        imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);*/
        return convertView;
    }
}
