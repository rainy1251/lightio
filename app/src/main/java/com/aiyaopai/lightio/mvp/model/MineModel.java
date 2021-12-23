package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.bean.UserBean;
import com.aiyaopai.lightio.mvp.contract.MineContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.UiUtils;
import com.aiyaopai.lightio.view.CacheDataManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class MineModel implements MineContract.Model {
    @Override
    public Observable<UserBean> getInfo() {
        return RetrofitClient.getServer().getUserInfo();
    }

    @Override
    public void signOut() {
        SPUtils.save(Contents.access_token,"");
    }

    @Override
    public Observable<Boolean> cleanCache() {

        return Observable.create(emitter -> {
            CacheDataManager.clearAllCache(UiUtils.getContext());
            emitter.onNext(true);
            emitter.onComplete();
        });
    }


}
