package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.OriginalPicBean;
import com.aiyaopai.lightio.mvp.contract.NoticeContract;
import com.aiyaopai.lightio.net.RetrofitClient;

import io.reactivex.rxjava3.core.Observable;

public class NoticeModel implements NoticeContract.Model {

    @Override
    public Observable<OriginalPicBean> getOriginalPic(int offset, String albumId, String photographerId) {
        return RetrofitClient.getServer().getOriginalPic(offset,50,albumId,photographerId);
    }

}
