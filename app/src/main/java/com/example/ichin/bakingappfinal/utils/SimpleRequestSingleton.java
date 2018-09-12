package com.example.ichin.bakingappfinal.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SimpleRequestSingleton {

    private static SimpleRequestSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private SimpleRequestSingleton(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

    }

    public static synchronized SimpleRequestSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SimpleRequestSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}