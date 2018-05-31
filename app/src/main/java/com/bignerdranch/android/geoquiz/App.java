package com.bignerdranch.android.geoquiz;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.math.BigDecimal;

/**
 * Created by yuguole on 2018/4/11.
 */

public class App extends Application {
    private static RequestQueue requestQueue;


    @Override
    public void onCreate(){
        super.onCreate();
        requestQueue=Volley.newRequestQueue(this);
    }
    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }

}
