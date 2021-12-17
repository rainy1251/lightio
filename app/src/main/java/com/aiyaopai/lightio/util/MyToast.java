package com.aiyaopai.lightio.util;

import android.widget.Toast;

public class MyToast {
    public static void show(String str){
        Toast.makeText(UiUtils.getContext(),str,Toast.LENGTH_SHORT).show();
    }



}
