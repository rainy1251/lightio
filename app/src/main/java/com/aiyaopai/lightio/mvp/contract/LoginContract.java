package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;

import io.reactivex.rxjava3.core.Observable;


public interface LoginContract {

    interface Model {
        Observable<SignInBean> loginPwd(String phoneNo, String pwd);
        Observable<SignInBean> loginCode(String phoneNo, String code);

        Observable<BaseBean> getCode(String phoneNo , String randStr, String ticket);
    }

    interface Presenter {
        void login(String userName, String pwd);
        void loginCode(String userName, String pwd);

        void getCode(String phoneNo , String randStr, String ticket);
    }

    interface View extends BaseView {
        void onSuccess(SignInBean signInBean);
        void onLoginCodeSuccess(SignInBean signInBean);
        void onGetCodeSuccess(BaseBean baseBean);
    }
}
