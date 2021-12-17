package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.BannerBean;
import com.aiyaopai.lightio.mvp.contract.HomeContract;
import com.aiyaopai.lightio.mvp.model.HomeModel;
import com.aiyaopai.lightio.net.RxScheduler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    private HomeContract.Model model;

    public HomePresenter() {
        model = new HomeModel();
    }

    @Override
    public void banner() {
        if (!isViewAttached()) {
            return;
        }
        model.banner()
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<List<BannerBean>>(mView) {
                    @Override
                    public void onNext(@NotNull List<BannerBean> banners) {
                        mView.onSuccess(banners);
                    }
                });
    }
}
