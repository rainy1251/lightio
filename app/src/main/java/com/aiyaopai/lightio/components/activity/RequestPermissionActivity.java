package com.aiyaopai.lightio.components.activity;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseActivity;
import com.aiyaopai.lightio.databinding.ActivityRequestPermissionBinding;

public class RequestPermissionActivity extends BaseActivity<ActivityRequestPermissionBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_request_permission;
    }

    @Override
    public void initView() {
        finish();
    }

    @Override
    protected void initData() {

    }
}