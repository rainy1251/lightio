package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;

import io.reactivex.rxjava3.core.Observable;

public interface MineContract {

    interface Model {
        Observable<SignInBean> getInfo(String userId);

        Observable<BaseBean> signOut();

        Observable<Boolean>  cleanCache();
    }

    interface Presenter {
        void getUserInfo(String userId);
        void signOut();
        void cleanCache();
    }

    interface View extends BaseView {
        void onSuccess(SignInBean bean);

        void onSignOutSuccess(BaseBean bean);

        void onClean();

    }

}
