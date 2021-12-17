package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.mvp.contract.NoticeContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class NoticeModel implements NoticeContract.Model {

    @Override
    public Observable<BaseBean> getUpLoadToken(String activityId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api,"Storage.GetOriginalPictureToken");
        map.put("ActivityId",activityId);
        return RetrofitClient.getServer().getQiNiuToken(map);
    }

    @Override
    public Observable<ActivityListBean> getOriginalPic(String activityId, String photographerId, int pageIndex) {

        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api,"OriginalPicture.Search");
        map.put(Contents.Fields,"Id,LocalKey,Size,OriginalAt,Url");
        map.put(Contents.PageIndex,pageIndex);
        map.put(Contents.PageSize,50);
        map.put("ActivityId",activityId);
        map.put("PhotographerId",photographerId);
        return RetrofitClient.getServer().getOriginalPic(map);
    }

}
