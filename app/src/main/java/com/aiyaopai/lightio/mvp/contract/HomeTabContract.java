package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.AlbumListBean;

import io.reactivex.rxjava3.core.Observable;

public interface HomeTabContract {
    interface Model {
        Observable<AlbumListBean> activitySearch(int pageIndex, String tagStr);
    }

    interface View extends BaseView {
       void  onSuccess(AlbumListBean albumListBean);
    }

    interface Presenter {
        void activitySearch(int pageIndex,String tagStr);
    }
}
