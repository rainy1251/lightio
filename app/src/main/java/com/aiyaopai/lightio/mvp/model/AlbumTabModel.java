package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.AlbumListBean;
import com.aiyaopai.lightio.mvp.contract.AlbumTabContract;
import com.aiyaopai.lightio.net.RetrofitClient;

import io.reactivex.rxjava3.core.Observable;

public class AlbumTabModel implements AlbumTabContract.Model {
    @Override
    public Observable<AlbumListBean> getList(int pageIndex) {
        return RetrofitClient.getServer().getAlbumList(pageIndex,10,"Owner");
    }
}
