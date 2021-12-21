package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.mvp.contract.ActivityTabContract;
import com.aiyaopai.lightio.net.RetrofitClient;

import io.reactivex.rxjava3.core.Observable;

public class ActivityTabModel implements ActivityTabContract.Model {
    @Override
    public Observable<ActivityListBean> getList(int pageIndex) {
        return RetrofitClient.getServer().getActivityList(pageIndex,10,"Owner");
    }
}
