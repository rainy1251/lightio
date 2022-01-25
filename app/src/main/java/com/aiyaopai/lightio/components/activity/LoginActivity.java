package com.aiyaopai.lightio.components.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseMvpActivity;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.databinding.ActivityLoginBinding;
import com.aiyaopai.lightio.event.LoginExitEvent;
import com.aiyaopai.lightio.event.LoginSuccessEvent;
import com.aiyaopai.lightio.mvp.contract.LoginContract;
import com.aiyaopai.lightio.mvp.presenter.LoginPresenter;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.TimerCount;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseMvpActivity<LoginPresenter, ActivityLoginBinding>
        implements LoginContract.View, View.OnClickListener {

    private LoginPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        viewBinding.includeLogin.ivIcon.setVisibility(View.VISIBLE);
        viewBinding.includeLogin.ivBack.setVisibility(View.GONE);
        viewBinding.includeLogin.tvToolbarTitle.setVisibility(View.GONE);
        initListener();
    }

    private void initListener() {
        viewBinding.tvSendCode.setOnClickListener(this);
        viewBinding.ivLogin.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        presenter = new LoginPresenter(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onLoginCodeSuccess(SignInBean bean) {
        SPUtils.save(Contents.access_token, bean.getAccess_token());
        SPUtils.save(Contents.refresh_token, bean.getRefresh_token());
      //  SPUtils.save(Contents.tokenBeginAt,System.currentTimeMillis());
        SPUtils.save(Contents.tokenBeginAt,1642556691000L);
        MyToast.show("登录成功");
        EventBus.getDefault().post(new LoginSuccessEvent(true));
        finish();
    }

    @Override
    public void onGetCodeSuccess(BaseBean baseBean) {
        if (baseBean.getMessage().equals(Contents.Success)) {
            TimerCount timer = new TimerCount(60000, 1000, viewBinding.tvSendCode);
            timer.start();
            MyToast.show("验证码已发送");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_login:
                String phoneNo = viewBinding.etPhone.getText().toString();
                String code = viewBinding.etCode.getText().toString();
                if (TextUtils.isEmpty(phoneNo)) {
                    MyToast.show("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    MyToast.show("请输入验证码");
                    return;
                }
                presenter.loginCode(phoneNo, code);
                break;
            case R.id.tv_sendCode:
                 phoneNo = viewBinding.etPhone.getText().toString();
                if (TextUtils.isEmpty(phoneNo)) {
                    MyToast.show("请输入手机号");
                    return;
                }
                presenter.getCode(phoneNo);

                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String access_token = SPUtils.getString(Contents.access_token);
        if (TextUtils.isEmpty(access_token)) {
            EventBus.getDefault().post(new LoginExitEvent());
        }
    }
}