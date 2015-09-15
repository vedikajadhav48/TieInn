package com.example.vedikajadhav.tieinn;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vedikajadhav.tieinnLibrary.AppController;
import com.example.vedikajadhav.tieinnLibrary.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateAccountActivity extends ActionBarActivity {
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

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTRATION_URL = "http://tieinn.comuv.com/registration.php?";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PROFILE_NAME = "profileName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mProfileNameEditText = (EditText)findViewById(R.id.edit_text_profile_name);
        mUsernameEditText = (EditText) findViewById(R.id.edit_text_username);
        mPasswordEditText = (EditText) findViewById(R.id.edit_text_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.edit_text_confirm_password);
        mCreateAccountButton = (Button)findViewById(R.id.create_account_button);
    }

    public void createAccount(View button){
        mProfileName = mProfileNameEditText.getText().toString();
        mUsername = mUsernameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString();

        if(mPassword.equals(mConfirmPassword)){
            new AttemptRegistration().execute();
            //registerUserOnNetwork();
        }else{
            Toast.makeText(CreateAccountActivity.this, "password and confirmPassword values do not match", Toast.LENGTH_LONG).show();
        }
    }

    public void registerUserOnNetwork(){
/*        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, REGISTRATION_URL, null,
                new Response.Listener<JSONObject>() {
                    // here Check for success tag
                    int success;
                    String message;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        try {
                            // success tag for json
                            success = response.getInt(TAG_SUCCESS);
                            message = response.getString(TAG_MESSAGE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //responseCode = json.getStatusLine().getStatusCode();
                        if (success == 1) {
                            Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
                            // this finish() method is used to tell android os that we are done with current
                            // activity now! Moving to other activity
                            finish();
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("profileName", mProfileName);
                params.put("username", mUsername);
                params.put("password", mPassword);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    class AttemptRegistration extends AsyncTask<String, String, String> {
        /** * Before starting background thread Show Progress Dialog * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateAccountActivity.this);
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;
            String profileName = mProfileNameEditText.getText().toString();
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("profileName", profileName));
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest( REGISTRATION_URL, "POST", params);
                // checking log for json response
                Log.d("Registration attempt", json.toString());
                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());
                    Intent ii = new Intent(CreateAccountActivity.this,LoginActivity.class);
                    finish();
                    // this finish() method is used to tell android os that we are done with current
                    // activity now! Moving to other activity
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }else{
                    return json.getString(TAG_MESSAGE);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /** * Once the background process is done we need to Dismiss the progress dialog asap * **/
        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null){
                Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
