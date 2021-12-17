package com.aiyaopai.lightio.components.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseActivity;
import com.aiyaopai.lightio.databinding.ActivityWebBinding;
import com.aiyaopai.lightio.util.Contents;

public class WebActivity extends BaseActivity<ActivityWebBinding> {

    private String path;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        viewBinding.includeWeb.ivIcon.setVisibility(View.GONE);
        viewBinding.includeWeb.ivBack.setVisibility(View.VISIBLE);
        viewBinding.includeWeb.tvToolbarTitle.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        path = getIntent().getStringExtra(Contents.UrlPath);
        String title = getIntent().getStringExtra(Contents.UrlTitle);
        viewBinding.includeWeb.tvToolbarTitle.setText(title);

        viewBinding.wvView.loadUrl(path);
        viewBinding.wvView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = viewBinding.wvView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置 缓存模式
        viewBinding.wvView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        // 开启 DOM storage API 功能
        viewBinding.wvView.getSettings().setDomStorageEnabled(true);

        viewBinding.wvView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (viewBinding != null) {
                    viewBinding.pbBar.setVisibility(View.VISIBLE);
                    viewBinding.pbBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        viewBinding.pbBar.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        viewBinding.includeWeb.ivBack.setOnClickListener(v -> finish());
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                intent.setData(Uri.parse("tel:400-021-0916"));
                startActivity(intent);
                view.reload();
                return false;
            }
            if (!url.contains("text/plain,")) {
//                if (url.contains("http://p.qiao.baidu.com/cps/mobileChat")) {
//                    if (isBaidu) {
//                        view.loadUrl(url);
//                        isBaidu = false;
//                    }
//                    return false;
//                }


                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && viewBinding.wvView.canGoBack()) {
            viewBinding.wvView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            viewBinding.wvView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void start(Activity context, String path, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Contents.UrlPath, path);
        intent.putExtra(Contents.UrlTitle, title);
        context.startActivity(intent);
    }

}