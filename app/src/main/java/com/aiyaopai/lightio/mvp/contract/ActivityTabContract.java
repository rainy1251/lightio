package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.ActivityListBean;

import io.reactivex.rxjava3.core.Observable;


public interface ActivityTabContract {
    interface Model {
        Observable<ActivityListBean> getList(int pageIndex);
    }

    interface View extends BaseView {
        void onSuccess(ActivityListBean bean);
    }

    interface Presenter {
        void getList(int pageIndex);
    }




}
