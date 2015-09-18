package com.example.vedikajadhav.tieinnLibrary;

import android.view.View;
import android.widget.Button;

import com.example.vedikajadhav.tieinn.R;

/**
 * Created by Vedika Jadhav on 9/17/2015.
 */
public class YourWrapper {
    private View base;
    private Button button;

    public YourWrapper(View base)
    {
        this.base = base;
    }

    public Button getButton()
    {
        if (button == null)
        {
            button = (Button) base.findViewById(R.id.discussion_board_write_answer_button);
        }
        return (button);
    }
}
