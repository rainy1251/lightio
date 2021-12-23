package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.bean.UserBean;
import com.aiyaopai.lightio.mvp.contract.MineContract;
import com.aiyaopai.lightio.mvp.model.MineModel;
import com.aiyaopai.lightio.net.RxScheduler;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.SPUtils;

import org.jetbrains.annotations.NotNull;

public class MinePresenter extends BasePresenter<MineContract.View> implements MineContract.Presenter {


    private final MineContract.Model model;

    public MinePresenter(MineContract.View view) {
        super(view);
        model = new MineModel();
    }

    @Override
    public void getUserInfo() {
   
        model.getInfo()
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<UserBean>(getView()) {
                    @Override
                    public void onNext(@NotNull UserBean bean) {
                        getView().onSuccess(bean);
                    }
                });
    }

    @Override
    public void signOut() {
        model.signOut();
        getView().onSignOutSuccess();
    }

    @Override
    public void cleanCache() {
        model.cleanCache().compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(t -> {
                    if (t) {
                        getView().onClean();
                    }
                });
    }


}
