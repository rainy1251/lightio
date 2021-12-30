package com.aiyaopai.lightio.mvp.presenter;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.AlbumListBean;
import com.aiyaopai.lightio.mvp.contract.AlbumTabContract;
import com.aiyaopai.lightio.mvp.model.AlbumTabModel;
import com.aiyaopai.lightio.net.RxScheduler;
import com.aiyaopai.lightio.util.Contents;

import org.jetbrains.annotations.NotNull;

public class AlbumTabPresenter extends BasePresenter<AlbumTabContract.View> implements AlbumTabContract.Presenter{

    private final AlbumTabContract.Model model;

    public AlbumTabPresenter(AlbumTabContract.View view) {
        super(view);
        model = new AlbumTabModel();
    }

    @Override
    public void getList(int pageIndex,String state) {
        int offset = pageIndex * Contents.albumOffsetNum;
        model.getList(offset,state)
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<AlbumListBean>(getView()) {
                    @Override
                    public void onNext(@NotNull AlbumListBean bean) {
                        getView().onSuccess(bean);
                    }
                });
    }
}
