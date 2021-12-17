package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.BannerBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface HomeContract {
    interface Model {
        Observable<List<BannerBean>> banner();
    }

    interface View extends BaseView {
        void onSuccess(List<BannerBean> banners);
    }

    interface Presenter {
        void banner();
    }
}
