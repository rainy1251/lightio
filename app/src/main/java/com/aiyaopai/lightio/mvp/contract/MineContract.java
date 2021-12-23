package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.bean.UserBean;

import io.reactivex.rxjava3.core.Observable;

public interface MineContract {

    interface Model {
        Observable<UserBean> getInfo();

        void signOut();

        Observable<Boolean>  cleanCache();
    }

    interface Presenter {
        void getUserInfo();
        void signOut();
        void cleanCache();
    }

    interface View extends BaseView {
        void onSuccess(UserBean bean);

        void onSignOutSuccess();

        void onClean();

    }

}
