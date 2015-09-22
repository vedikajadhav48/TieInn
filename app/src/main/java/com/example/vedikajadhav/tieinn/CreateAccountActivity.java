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
import com.example.vedikajadhav.tieinnLibrary.JSONParser;
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
    private Button mCreateAccountButton;
    private static final int Intent_User_Index = 123;
    private EditText mProfileNameEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private String mProfileName;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword;
    private static PostCreateAccountResponseListener mPostCreateAccountListener;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mProfileNameEditText = (EditText)findViewById(R.id.edit_text_profile_name);
        mUsernameEditText = (EditText) findViewById(R.id.edit_text_username);
        mPasswordEditText = (EditText) findViewById(R.id.edit_text_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.edit_text_confirm_password);
        mCreateAccountButton = (Button)findViewById(R.id.create_account_button);

        mPostCreateAccountListener = new PostCreateAccountResponseListener() {
            @Override
            public void requestStarted() {

            }

            @Override
            public void requestCompleted() {

                 Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
                            // this finish() method is used to tell android os that we are done with current
                            // activity now! Moving to other activity
                            finish();
                            startActivity(intent);
            }

            @Override
            public void requestEndedWithError(VolleyError error) {

            }
        };
    }

    public void createAccount(View button){
        mProfileName = mProfileNameEditText.getText().toString();
        mUsername = mUsernameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString();

        if(mPassword.equals(mConfirmPassword)){
            //new AttemptRegistration().execute();
            registerUserOnNetwork(this, mProfileName, mUsername, mPassword);
        }else{
            CustomAlertDialog.showAlertDialog(this, "Password  and/or ConfirmPassword field empty", "Password and ConfirmPassword values do not match");
        }
    }

    public void registerUserOnNetwork(Context context, final String profileName, final String username, final String password){
        /*ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();*/

        mPostCreateAccountListener.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Constants.REGISTRATION_URL,
                new Response.Listener<String>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());
                        mPostCreateAccountListener.requestCompleted();

                        /*JSONObject json = jsonParser.makeHttpRequest( Constants.REGISTRATION_URL, "POST", params);
                        // checking log for json response
                        Log.d("Registration attempt", json.toString());
                        // success tag for json
                        success = json.getInt(Constants.TAG_SUCCESS);*/
                        /*try {
                            // success tag for json
                            success = response.getInt(Constants.TAG_SUCCESS);
                            message = response.getString(Constants.TAG_MESSAGE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        //responseCode = json.getStatusLine().getStatusCode();
                        if (success == 1) {
                            mPostCreateAccountListener.requestCompleted();

                            /*Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
                            // this finish() method is used to tell android os that we are done with current
                            // activity now! Moving to other activity
                            finish();
                            startActivity(intent);*/
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mPostCreateAccountListener.requestEndedWithError(error);
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
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
        queue.add(jsonObjReq);
    }
}
