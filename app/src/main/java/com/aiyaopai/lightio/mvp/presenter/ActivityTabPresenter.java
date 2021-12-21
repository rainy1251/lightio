package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.mvp.contract.ActivityTabContract;
import com.aiyaopai.lightio.mvp.model.ActivityTabModel;
import com.aiyaopai.lightio.net.RxScheduler;

import org.jetbrains.annotations.NotNull;

public class ActivityTabPresenter extends BasePresenter<ActivityTabContract.View> implements ActivityTabContract.Presenter{

    private final ActivityTabContract.Model model;

    public ActivityTabPresenter(ActivityTabContract.View view) {
        super(view);
        model = new ActivityTabModel();
    }

    @Override
    public void getList(int pageIndex) {
        model.getList(pageIndex)
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<ActivityListBean>(getView()) {
                    @Override
                    public void onNext(@NotNull ActivityListBean bean) {
                        getView().onSuccess(bean);
                    }
                });
    }
}
