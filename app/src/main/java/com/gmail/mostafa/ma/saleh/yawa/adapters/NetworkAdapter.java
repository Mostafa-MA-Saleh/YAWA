package com.gmail.mostafa.ma.saleh.yawa.adapters;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mostafa Saleh on 04/29/2017.
 */

public class NetworkAdapter {

    private static NetworkAdapter mInstance;
    private RequestQueue mRequestQueue;

    private NetworkAdapter(){}

    public static NetworkAdapter getInstance(){
        if (mInstance == null) mInstance = new NetworkAdapter();
        return mInstance;
    }

    public RequestQueue getRequestQueue(Context context){
        if(mRequestQueue == null) mRequestQueue = Volley.newRequestQueue(context);
        return mRequestQueue;
    }
}
