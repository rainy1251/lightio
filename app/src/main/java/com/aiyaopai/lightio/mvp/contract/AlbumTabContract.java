package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.AlbumListBean;

import io.reactivex.rxjava3.core.Observable;


public interface AlbumTabContract {
    interface Model {
        Observable<AlbumListBean> getList(int offset,String state);
    }

    interface View extends BaseView {
        void onSuccess(AlbumListBean bean);
    }

    interface Presenter {
        void getList(int pageIndex,String state);
    }

}
