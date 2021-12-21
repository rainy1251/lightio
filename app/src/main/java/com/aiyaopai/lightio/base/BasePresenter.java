package com.aiyaopai.lightio.base;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends BaseView> {

    private WeakReference<V> referenceView;

    public BasePresenter(V view) {
        attachView(view);
    }
    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    public void attachView(V view) {

        if (view == null)
            throw new NullPointerException("view can not be null when in attachview() in BasePresenter");
        else {
            if (referenceView == null)
                referenceView = new WeakReference<>(view);//将View置为弱引用，当view被销毁回收时，依赖于view的对象（即Presenter）也会被回收，而不会造成内存泄漏 }

        }
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        if (referenceView != null) {
            referenceView.clear();
            referenceView = null;
        }
    }

    public V getView() {
        if (isAttachView()) return referenceView.get();
        else throw new NullPointerException("have you ever called attachView() in BasePresenter");
    }

    public boolean isAttachView() {
        return referenceView != null && referenceView.get() != null;
    }


}