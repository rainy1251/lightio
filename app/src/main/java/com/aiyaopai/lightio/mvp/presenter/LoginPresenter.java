package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.mvp.contract.LoginContract;
import com.aiyaopai.lightio.mvp.model.LoginModel;
import com.aiyaopai.lightio.net.RxScheduler;

import org.jetbrains.annotations.NotNull;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private final LoginContract.Model model;

    public LoginPresenter() {
        model = new LoginModel();
    }

    @Override
    public void login(String phoneNo, String pwd) {
        if (!isViewAttached()) {
            return;
        }
        model.loginPwd(phoneNo,pwd).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<SignInBean>(mView) {
                    @Override
                    public void onNext(@NotNull SignInBean bean) {
                        mView.onSuccess(bean);
                    }
                });
    }

    @Override
    public void loginCode(String phoneNo, String pwd) {
        if (!isViewAttached()) {
            return;
        }
        model.loginCode(phoneNo,pwd).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<SignInBean>(mView) {
                    @Override
                    public void onNext(@NotNull SignInBean bean) {
                        mView.onSuccess(bean);
                    }
                });
    }

    @Override
    public void getCode(String phoneNo, String randStr, String ticket) {
        model.getCode(phoneNo,randStr,ticket).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<BaseBean>(mView) {
                    @Override
                    public void onNext(@NotNull BaseBean bean) {
                        mView.onGetCodeSuccess(bean);
                    }
                });
    }
}
