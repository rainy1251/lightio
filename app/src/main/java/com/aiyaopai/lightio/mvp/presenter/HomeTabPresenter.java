package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.AlbumListBean;
import com.aiyaopai.lightio.mvp.contract.HomeTabContract;
import com.aiyaopai.lightio.mvp.model.HomeTabModel;
import com.aiyaopai.lightio.net.RxScheduler;

import org.jetbrains.annotations.NotNull;

public class HomeTabPresenter extends BasePresenter<HomeTabContract.View> implements HomeTabContract.Presenter {

    private  HomeTabContract.Model tabModel;

    public HomeTabPresenter(HomeTabContract.View view) {
        super(view);
        tabModel = new HomeTabModel();
    }

    @Override
    public void activitySearch(int pageIndex, String tagStr) {
    
        tabModel.activitySearch(pageIndex,tagStr)
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<AlbumListBean>(getView()) {
                    @Override
                    public void onNext(@NotNull AlbumListBean bean) {
                        getView().onSuccess(bean);
                    }
                });
    }
}
