package com.aiyaopai.lightio.base;

import com.aiyaopai.lightio.net.OnErrorCalBackListener;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyLog;
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
            switch (code) {
                case Contents.Code_401:
//                    AsyncRun.runInMain(new Runnable() {
//                        @Override
//                        public void run() {
//                            mView.onAgainLogin();
//                        }
//                    });
//
//                    break;
                case Contents.Code_400:

                case Contents.Code_403:


                case Contents.Code_404:


                case Contents.Code_405:

                case Contents.Code_415:

                case Contents.Code_500:


                default:
                    mView.onError(msg);

                    break;
            }
        } catch (Exception e) {
                MyLog.show(e.toString());
        }
    }
}
