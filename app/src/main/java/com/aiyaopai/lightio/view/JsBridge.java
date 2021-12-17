package com.aiyaopai.lightio.view;

import android.webkit.JavascriptInterface;

public class JsBridge {
    @JavascriptInterface
    public void getData(String data) {
        if (onValidateListener != null) {
            onValidateListener.getValidate(data);
        }
    }

    public interface OnValidateListener{
        void getValidate(String data);
    }

    public void setOnValidateListener(OnValidateListener onValidateListener) {
        this.onValidateListener = onValidateListener;
    }

    public OnValidateListener onValidateListener;
}