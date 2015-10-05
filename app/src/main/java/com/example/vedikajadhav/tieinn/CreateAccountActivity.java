package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vedikajadhav.tieinnLibrary.AppController;
import com.example.vedikajadhav.tieinnLibrary.CustomAlertDialog;
import com.example.vedikajadhav.tieinnLibrary.PostCreateAccountResponseListener;
import com.example.vedikajadhav.tieinnModel.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateAccountActivity extends ActionBarActivity{
    private static final String TAG= "CreateAccountActivity";
    private EditText mProfileNameEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private String mProfileName;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mProfileNameEditText = (EditText)findViewById(R.id.edit_text_profile_name);
        mUsernameEditText = (EditText) findViewById(R.id.edit_text_username);
        mPasswordEditText = (EditText) findViewById(R.id.edit_text_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.edit_text_confirm_password);
    }

    public void createAccount(View button){
        mProfileName = mProfileNameEditText.getText().toString();
        mUsername = mUsernameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString();

        if(mProfileName.isEmpty() || mUsername.isEmpty() || mPassword.isEmpty() || mConfirmPassword.isEmpty()){
            CustomAlertDialog.showAlertDialog(this, "Empty Field", "Enter values in all the fields");
        }else if(!mPassword.equals(mConfirmPassword)){
            CustomAlertDialog.showAlertDialog(this, "Password  and/or ConfirmPassword field empty", "Password and ConfirmPassword values do not match");
        }else{
            registerUserOnNetwork(this, mProfileName, mUsername, mPassword);
        }
    }

    public void registerUserOnNetwork(final Context context, final String profileName, final String username, final String password){
        /*ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();*/

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTRATION_URL,
                new Response.Listener<String>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject person = new JSONObject(response);
                            success = person.getInt("success");
                            message = person.getString("message");

                            if (success == 1) {
                                Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
                                // this finish() method is used to tell android os that we are done with current
                                // activity now! Moving to other activity
                                finish();
                                startActivity(intent);
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Error Occurred", "Account could not be created");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("profileName", profileName);
                params.put("username", username);
                params.put("password", password);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        queue.add(stringRequest);
    }
}
