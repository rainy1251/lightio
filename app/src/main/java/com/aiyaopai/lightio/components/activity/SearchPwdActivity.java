package com.aiyaopai.lightio.components.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseMvpActivity;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.databinding.ActivitySearchPwdBinding;
import com.aiyaopai.lightio.mvp.contract.SearchPwdContract;
import com.aiyaopai.lightio.mvp.presenter.SearchPwdPresenter;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.view.TencentValidateDialog;
import com.aiyaopai.lightio.view.TimerCount;
import com.qiniu.android.utils.AsyncRun;

public class SearchPwdActivity extends BaseMvpActivity<SearchPwdPresenter, ActivitySearchPwdBinding>
        implements SearchPwdContract.View, TencentValidateDialog.OnValidateSuccessListener, View.OnClickListener {

    private SearchPwdPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_pwd;
    }

    @Override
    public void initView() {
        viewBinding.includeReset.ivIcon.setVisibility(View.GONE);
        viewBinding.includeReset.ivBack.setVisibility(View.VISIBLE);
        viewBinding.includeReset.tvToolbarTitle.setVisibility(View.VISIBLE);
        viewBinding.includeReset.tvToolbarTitle.setText("修改密码");
    }

    @Override
    protected void initData() {
        presenter = new SearchPwdPresenter(this);
//        presenter.attachView(this);
        initListener();
    }

    private void initListener() {
        viewBinding.includeReset.ivBack.setOnClickListener(this);
        viewBinding.tvGetcode.setOnClickListener(this);
        viewBinding.tvOk.setOnClickListener(this);
    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, SearchPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(BaseBean bean) {
        if (bean.isSuccess()) {
            MyToast.show("修改成功");
            finish();
        }

    }

    @Override
    public void onGetCodeSuccess(BaseBean bean) {
        if (bean.isSuccess()) {
            TimerCount timer = new TimerCount(60000, 1000, viewBinding.tvGetcode);
            timer.start();
            MyToast.show("验证码已发送");
        }

    }

    @Override
    public void validateSuccess(String randStr, String ticket) {
        String phoneNo = viewBinding.etPhone.getText().toString();
        AsyncRun.runInMain(new Runnable() {
            @Override
            public void run() {
                presenter.sendCode(phoneNo, randStr, ticket);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String phoneNo;
        switch (v.getId()) {
            case R.id.tv_getcode:
                phoneNo = viewBinding.etPhone.getText().toString();
                if (TextUtils.isEmpty(phoneNo)) {
                    MyToast.show("请输入手机号");
                    return;
                }
                TencentValidateDialog dialog = new TencentValidateDialog(SearchPwdActivity.this);
                dialog.setOnValidateSuccessListener(this);
                dialog.show();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_ok:
                String code = viewBinding.etCode.getText().toString();
                String pwd = viewBinding.etPwd.getText().toString();
                phoneNo = viewBinding.etPhone.getText().toString();
                if (TextUtils.isEmpty(phoneNo)) {
                    MyToast.show("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    MyToast.show("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    MyToast.show("请输入新密码");
                    return;
                }
                presenter.resetPwd(phoneNo, code, pwd);
                break;
        }
    }
}
