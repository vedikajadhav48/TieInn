package com.example.vedikajadhav.tieinnLibrary;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vedika Jadhav on 5/14/2015.
 */
public class UserFunctions {

        private JSONParser jsonParser;

        //URL of the PHP API
        private static String loginURL = "http://tiein.comyr.com/api.php?";
        private static String registerURL = "http://tiein.comyr.com/api.php?";
        private static String forpassURL = "http://tiein.comyr.com/api.php?";
        private static String chgpassURL = "http://tiein.comyr.com/api.php?";


        private static String login_tag = "login";
        private static String register_tag = "register";
        private static String forpass_tag = "forpass";
        private static String chgpass_tag = "chgpass";


        // constructor
        public UserFunctions(){
            jsonParser = new JSONParser();
        }

        /**
         * Function to Login
         **/

        public JSONObject loginUser(String username, String password){
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", login_tag));
            params.add(new BasicNameValuePair("email", username));
            params.add(new BasicNameValuePair("password", password));
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
            return json;
        }

        /**
         * Function to change password
         **/

        public JSONObject chgPass(String newpas, String email){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", chgpass_tag));

            params.add(new BasicNameValuePair("newpas", newpas));
            params.add(new BasicNameValuePair("email", email));
            JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
            return json;
        }

        /**
         * Function to reset the password
         **/

        public JSONObject forPass(String forgotpassword){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", forpass_tag));
            params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
            JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
            return json;
        }

        /**
         * Function to  Register
         **/
        public JSONObject registerUser(String fname, String lname, String email, String uname, String password){
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", register_tag));
            params.add(new BasicNameValuePair("fname", fname));
            params.add(new BasicNameValuePair("lname", lname));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("uname", uname));
            params.add(new BasicNameValuePair("password", password));
            JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
            return json;
        }


        /**
         * Function to logout user
         * Resets the temporary data stored in SQLite Database
         * */
        public boolean logoutUser(Context context){
            DatabaseHandler db = new DatabaseHandler(context);
            db.resetTables();
            return true;
        }

    }
