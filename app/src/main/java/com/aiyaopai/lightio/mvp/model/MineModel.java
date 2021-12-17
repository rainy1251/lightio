package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.mvp.contract.MineContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.UiUtils;
import com.aiyaopai.lightio.view.CacheDataManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class MineModel implements MineContract.Model {
    @Override
    public Observable<SignInBean> getInfo(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Photographer.Get");
        map.put(Contents.Id, userId);
        map.put(Contents.Fields, "Avatar,Nickname,ActivitiesCount,OriginalPicturesCount");
        return RetrofitClient.getServer().getUserInfo(map);
    }

    @Override
    public Observable<BaseBean> signOut() {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Account.SignOut");
        return RetrofitClient.getServer().signOutApi(map);
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
