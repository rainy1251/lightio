package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;

import io.reactivex.rxjava3.core.Observable;


public interface LoginContract {

    interface Model {
        Observable<SignInBean> loginCode(String phoneNo, String code);
        Observable<BaseBean> getCode(String phoneNo);
    }

    interface Presenter {

        void loginCode(String userName, String pwd);

        void getCode(String phoneNo);
    }

    interface View extends BaseView {

        void onLoginCodeSuccess(SignInBean signInBean);
        void onGetCodeSuccess(BaseBean baseBean);
    }
}
