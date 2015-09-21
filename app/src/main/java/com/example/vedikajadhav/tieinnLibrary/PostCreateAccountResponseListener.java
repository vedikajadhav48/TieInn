package com.example.vedikajadhav.tieinnLibrary;

import com.android.volley.VolleyError;

/**
 * Created by Vedika Jadhav on 9/21/2015.
 */
public interface PostCreateAccountResponseListener {
        public void requestStarted();
        public void requestCompleted();
        public void requestEndedWithError(VolleyError error);
}
