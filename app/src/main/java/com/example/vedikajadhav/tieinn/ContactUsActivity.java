package com.example.vedikajadhav.tieinn;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ContactUsActivity extends ActionBarActivity {
    private Button sendFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        sendFeedbackButton = (Button)findViewById(R.id.send_feedback_button);
        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent = Intent.createChooser(intent, getString(R.string.send_feedback_button_text));
                startActivity(intent);
            }
        });
    }
}
