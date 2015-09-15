package com.example.vedikajadhav.tieinnLibrary;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Vedika Jadhav on 9/15/2015.
 */
public class CustomAlertDialog {

    public void showAlertDialog(Context context, String title, String message){
        new android.app.AlertDialog.Builder(context)
                .setTitle("Username and/or Password field empty")
                .setMessage("Enter Username and Password")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
