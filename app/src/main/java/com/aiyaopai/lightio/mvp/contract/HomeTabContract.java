package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.ActivityListBean;

import io.reactivex.rxjava3.core.Observable;

public interface HomeTabContract {
    interface Model {
        Observable<ActivityListBean> activitySearch(int pageIndex,String tagStr);
    }

    interface View extends BaseView {
       void  onSuccess(ActivityListBean activityListBean);
    }

    interface Presenter {
        void activitySearch(int pageIndex,String tagStr);
    }
}
