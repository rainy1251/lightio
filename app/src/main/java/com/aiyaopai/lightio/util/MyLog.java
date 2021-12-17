package com.aiyaopai.lightio.util;


import android.util.Log;

/**
 * Created by Administrator on 2018/1/18.
 */

public class MyLog {
    private static final boolean isDeBug = true;
    public static void show(Object value){
        if (isDeBug){

            Log.i("rain",value.toString());
        }
    }
}
