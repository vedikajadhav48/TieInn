package vedikajadhav.sdsu.thesis.aztecFAQ;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vedikajadhav.tieinn.R;

import vedikajadhav.sdsu.thesis.aztecFAQLibrary.SessionManager;

public class CategoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static final String TAG= "CategoryActivity";
    private ListView categoryListView;
    SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Session class instance
        mSession = SessionManager.getInstance(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(mSession.checkLogin()) {
            finish();
        }

        categoryListView = (ListView)findViewById(R.id.category_list_view);
        ArrayAdapter<String> categoryListViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.category_list_menu_items));
        categoryListView.setAdapter(categoryListViewAdapter);
        categoryListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //ListView clicked item value
        String category = (String)categoryListView.getItemAtPosition(position);
        Intent intent1 = new Intent(getApplicationContext(), DiscussionBoardActivity.class);
        intent1.putExtra(DiscussionBoardActivity.Intent_category, category);
        startActivity(intent1);
    }
}
