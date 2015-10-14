package vedikajadhav.sdsu.thesis.aztecFAQ;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vedikajadhav.tieinn.R;

import vedikajadhav.sdsu.thesis.aztecFAQLibrary.CustomAlertDialog;
import vedikajadhav.sdsu.thesis.aztecFAQModel.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
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
                            JSONObject jsonObjectResponse = new JSONObject(response);
                            success = jsonObjectResponse.getInt("success");
                            message = jsonObjectResponse.getString("message");

                            if(success == 1) {
                                finish();
                            }
                            else{
                                CustomAlertDialog.showAlertDialog(context, "Error Occurred", message);
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
                Log.i(TAG,"params createAccount" + params);

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
