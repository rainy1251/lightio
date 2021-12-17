package com.aiyaopai.lightio.base;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class CommonObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    abstract public void onNext(T t);

    @Override
    public void onError(@NonNull Throwable e) {
    }

    @Override
    public void onComplete() {

    }

    public CommonObserver() {
        super();

    }

}
