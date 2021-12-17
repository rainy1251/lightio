package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.BaseBean;

import io.reactivex.rxjava3.core.Observable;

public interface SearchPwdContract {
    interface Model {
        Observable<BaseBean> resetPwd(String phoneNo, String code, String pwd);
        Observable<BaseBean> sendCode(String phoneNo ,String randStr, String ticket);
    }

    interface View extends BaseView {
        void onSuccess(BaseBean bean);
        void onGetCodeSuccess(BaseBean bean);
    }

    interface Presenter {
        void resetPwd(String phoneNo ,String randStr, String ticket);
        void sendCode(String phoneNo ,String randStr, String ticket);
    }

}
