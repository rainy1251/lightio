package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.mvp.contract.SearchPwdContract;
import com.aiyaopai.lightio.mvp.model.SearchPwdModel;
import com.aiyaopai.lightio.net.RxScheduler;

import org.jetbrains.annotations.NotNull;

public class SearchPwdPresenter extends BasePresenter<SearchPwdContract.View> implements SearchPwdContract.Presenter {

    private  SearchPwdContract.Model model;

    public SearchPwdPresenter(SearchPwdContract.View view) {
        super(view);
        model = new SearchPwdModel();
    }

    @Override
    public void resetPwd(String phoneNo, String code, String pwd ){

        model.resetPwd(phoneNo,code,pwd)
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<BaseBean>(getView()) {
                    @Override
                    public void onNext(@NotNull BaseBean bean) {
                        getView().onSuccess(bean);
                    }
                });
    }

    @Override
    public void sendCode(String phoneNo ,String randStr, String ticket ){

        model.sendCode(phoneNo,randStr,ticket)
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<BaseBean>(getView()) {
                    @Override
                    public void onNext(@NotNull BaseBean bean) {
                        getView().onGetCodeSuccess(bean);
                    }
                });
    }
}
