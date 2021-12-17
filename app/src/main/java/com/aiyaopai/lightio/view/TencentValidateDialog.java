package com.aiyaopai.lightio.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.TencentValidate;
import com.google.gson.Gson;

import java.util.Objects;


/**
 * Created by Administrator on 2017/4/27.
 */


public class TencentValidateDialog extends Dialog implements JsBridge.OnValidateListener {


    public TencentValidateDialog(Context context) {

        this(context, 0);


    }

    public TencentValidateDialog(Context context, int themeResId) {
        super(context, themeResId);

        setCustomDialog();
    }


    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.validate_layout, null);
        WebView mWebView = mView.findViewById(R.id.wv_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 禁用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        // 开启js支持
        webSettings.setJavaScriptEnabled(true);
        JsBridge jsBridge = new JsBridge();
        mWebView.addJavascriptInterface(jsBridge, "jsBridge");
        jsBridge.setOnValidateListener(this);
        // 也可以加载本地html(webView.loadUrl("file:///android_asset/xxx.html"))
        mWebView.loadUrl("file:///android_asset/tencentvalidate.html");

        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);

        super.setContentView(mView);
    }


    @Override
    public void getValidate(String data) {
        if (!TextUtils.isEmpty(data)) {
            if (data.equals("close")) {
                dismiss();
                return;
            }
            try {
                Gson gson = new Gson();
                TencentValidate tencentValidate = gson.fromJson(data, TencentValidate.class);
                String randstr = tencentValidate.getRandstr();
                String ticket = tencentValidate.getTicket();
                if (onValidateSuccessListener != null) {
                    onValidateSuccessListener.validateSuccess(randstr, ticket);
                }
                dismiss();
            } catch (Exception e) {
            }

        }

    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = Objects.requireNonNull(getWindow()).getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(20, 0, 20, 0);

        getWindow().setAttributes(layoutParams);

    }

    public interface OnValidateSuccessListener {
        void validateSuccess(String randstr, String ticket);
    }

    public void setOnValidateSuccessListener(OnValidateSuccessListener onValidateSuccessListener) {
        this.onValidateSuccessListener = onValidateSuccessListener;
    }

    public OnValidateSuccessListener onValidateSuccessListener;
}