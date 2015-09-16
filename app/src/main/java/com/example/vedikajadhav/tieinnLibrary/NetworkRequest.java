package com.example.vedikajadhav.tieinnLibrary;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Vedika Jadhav on 9/15/2015.
 */
public class NetworkRequest {
    private static NetworkRequest mInstance = null;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    public interface ResponseListener {
        void onResponse(String response);
        void onError();
    }

    private NetworkRequest() {

        //mContext = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized NetworkRequest getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new NetworkRequest();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
