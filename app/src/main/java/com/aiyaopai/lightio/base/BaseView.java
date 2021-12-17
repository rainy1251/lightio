package com.aiyaopai.lightio.base;

import autodispose2.AutoDisposeConverter;

public interface BaseView {

    /**
     * 显示加载中
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    void onAgainLogin();
    /**
     * 数据获取失败
     * @param msg
     */
    void onError(String msg);

    /**
     * 绑定Android生命周期 防止RxJava内存泄漏
     *
     * @param <T>
     * @return
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();

}