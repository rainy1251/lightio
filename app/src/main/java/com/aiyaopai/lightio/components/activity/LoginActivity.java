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
import com.aiyaopai.lightio.event.LoginSuccessEvent;
import com.aiyaopai.lightio.mvp.contract.LoginContract;
import com.aiyaopai.lightio.mvp.presenter.LoginPresenter;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.TencentValidateDialog;
import com.aiyaopai.lightio.view.TimerCount;
import com.qiniu.android.utils.AsyncRun;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseMvpActivity<LoginPresenter, ActivityLoginBinding>
        implements LoginContract.View, View.OnClickListener, TencentValidateDialog.OnValidateSuccessListener {


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
        viewBinding.includeLogin.tvRight.setVisibility(View.VISIBLE);
        viewBinding.includeLogin.tvRight.setText("密码登录");
        initListener();
    }

    private void initListener() {
        viewBinding.tvReset.setOnClickListener(this);
        viewBinding.tvReset.setOnClickListener(this);
        viewBinding.includeLogin.tvRight.setOnClickListener(this);
        viewBinding.tvSendCode.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(SignInBean bean) {

        SPUtils.save(Contents.Token, bean.getToken());
        SPUtils.save(Contents.Id, bean.getId());
        MyToast.show("登录成功");
        EventBus.getDefault().post(new LoginSuccessEvent(true));
        finish();
    }

    @Override
    public void onLoginCodeSuccess(SignInBean bean) {
        SPUtils.save(Contents.Token, bean.getToken());
        SPUtils.save(Contents.Id, bean.getId());
        MyToast.show("登录成功");
        EventBus.getDefault().post(new LoginSuccessEvent(true));
        finish();
    }

    @Override
    public void onGetCodeSuccess(BaseBean baseBean) {
        if (baseBean.isSuccess()) {
            TimerCount timer = new TimerCount(60000, 1000, viewBinding.tvSendCode);
            timer.start();
            MyToast.show("验证码已发送");
        }
    }

    public void loginPwd(View view) {
        String phoneNo = viewBinding.etPhone.getText().toString();
        if (viewBinding.rlPwd.getVisibility() == View.VISIBLE) {
            String pwd = viewBinding.etPwd.getText().toString();
            if (TextUtils.isEmpty(phoneNo)) {
                MyToast.show("请输入正确的手机号");
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                MyToast.show("请输入密码");
                return;
            }
            presenter.login(phoneNo, pwd);
        } else {
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
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reset:
                SearchPwdActivity.start(LoginActivity.this);
                break;
            case R.id.tv_right:
                if (viewBinding.rlPwd.getVisibility() == View.VISIBLE) {
                    viewBinding.includeLogin.tvRight.setText("验证码登录");
                    viewBinding.rlCode.setVisibility(View.VISIBLE);
                    viewBinding.rlPwd.setVisibility(View.GONE);
                } else {
                    viewBinding.includeLogin.tvRight.setText("密码登录");
                    viewBinding.rlCode.setVisibility(View.GONE);
                    viewBinding.rlPwd.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_sendCode:
                String phoneNo = viewBinding.etPhone.getText().toString();
                if (TextUtils.isEmpty(phoneNo)) {
                    MyToast.show("请输入手机号");
                    return;
                }
                TencentValidateDialog dialog = new TencentValidateDialog(LoginActivity.this);
                dialog.setOnValidateSuccessListener(this);
                dialog.show();
                break;

        }
    }

    @Override
    public void validateSuccess(String randstr, String ticket) {
        String phoneNo = viewBinding.etPhone.getText().toString();
        AsyncRun.runInMain(() -> presenter.getCode(phoneNo, randstr, ticket));
    }
}