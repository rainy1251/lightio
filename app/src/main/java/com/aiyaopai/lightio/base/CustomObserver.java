package com.aiyaopai.lightio.base;

import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.net.OnErrorCalBackListener;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;
import com.alibaba.fastjson.JSON;
import com.qiniu.android.utils.AsyncRun;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class CustomObserver<T> implements Observer<T>, OnErrorCalBackListener {
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mView.showLoading();
    }

    @Override
    abstract public void onNext(T t);

    @Override
    public void onError(@NonNull Throwable e) {
        mView.hideLoading();
    }

    @Override
    public void onComplete() {
        mView.hideLoading();
    }

    private BaseView mView;

    public CustomObserver(BaseView view) {
        super();
        mView = view;
        RetrofitClient.setOnErrorCalBackListener(this);
    }

    @Override
    public void onError(int code, String msg) {
        if (code == Contents.Code_200 || code == Contents.Code_201) {

            return;
        }

        if (mView != null) {
            mView.hideLoading();
        }
        try {
            final BaseBean baseBean = JSON.parseObject(msg, BaseBean.class);
            switch (code) {
                case Contents.Code_401:
                    AsyncRun.runInMain(new Runnable() {
                        @Override
                        public void run() {
                            mView.onAgainLogin();
//                         ToastUtils.cusTostText(UiUtils.getContext(), ToastUtils.mViewIdText, "请先登录");
                        }
                    });

                    break;
                case Contents.Code_400:
                    if (msg.contains("301103")) {
                        mView.onError("用户名或密码错误");
                    } else if (msg.contains("301102")) {
                        mView.onError("密码连续错误三次用户已锁定, 请使用验证登录或找回密码!");
                    }

                    break;
                case Contents.Code_403:


                case Contents.Code_404:


                case Contents.Code_405:

                case Contents.Code_415:

                case Contents.Code_500:


                default:
                    mView.onError(baseBean.getMessage());

                    break;
            }
        } catch (Exception e) {

        }
    }
}
