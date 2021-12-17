package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.mvp.contract.MineContract;
import com.aiyaopai.lightio.mvp.model.MineModel;
import com.aiyaopai.lightio.net.RxScheduler;

import org.jetbrains.annotations.NotNull;

public class MinePresenter extends BasePresenter<MineContract.View> implements MineContract.Presenter {


    private final MineContract.Model model;

    public MinePresenter() {
        model = new MineModel();
    }

    @Override
    public void getUserInfo(String userId) {
        if (!isViewAttached()) {
            return;
        }
        model.getInfo(userId)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<SignInBean>(mView) {
                    @Override
                    public void onNext(@NotNull SignInBean bean) {
                        mView.onSuccess(bean);
                    }
                });
    }

    @Override
    public void signOut() {
        if (!isViewAttached()) {
            return;
        }
        model.signOut()
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<BaseBean>(mView) {
                    @Override
                    public void onNext(@NotNull BaseBean bean) {
                        mView.onSignOutSuccess(bean);
                    }
                });
    }

    @Override
    public void cleanCache() {
        model.cleanCache().compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(t -> {
                    if (t) {
                        mView.onClean();
                    }
                });
    }


}
