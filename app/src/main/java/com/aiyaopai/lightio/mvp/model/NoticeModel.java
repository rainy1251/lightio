package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.OriginalPicBean;
import com.aiyaopai.lightio.mvp.contract.NoticeContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.rxjava3.core.Observable;

public class NoticeModel implements NoticeContract.Model {

    @Override
    public Observable<BaseBean> getUpLoadToken(String albumId) {
        TimeZone tz = TimeZone.getDefault();
        int timezoneOffset = (tz.getRawOffset()) / (3600 * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("albumId",albumId);
        map.put("timezoneOffset",timezoneOffset);
        return RetrofitClient.getServer().getQiNiuToken(map);
    }

    @Override
    public Observable<OriginalPicBean> getOriginalPic(int pageIndex, String albumId, String photographerId) {
        return RetrofitClient.getServer().getOriginalPic(pageIndex,50,albumId,photographerId);
    }

}
