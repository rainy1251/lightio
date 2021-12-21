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

    public LoginPresenter(LoginContract.View view) {
        super(view);
        model = new LoginModel();
    }

    @Override
    public void loginCode(String phoneNo, String pwd) {

        model.loginCode(phoneNo, pwd).compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())
                .subscribe(new CustomObserver<SignInBean>(getView()) {
                    @Override
                    public void onNext(@NotNull SignInBean bean) {
                        getView().onLoginCodeSuccess(bean);
                    }
                });

    }

    @Override
    public void getCode(String phoneNo) {
        model.getCode(phoneNo).compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())
                .subscribe(new CustomObserver<BaseBean>(getView()) {
                    @Override
                    public void onNext(@NotNull BaseBean bean) {
                        getView().onGetCodeSuccess(bean);
                    }
                });
    }
}
