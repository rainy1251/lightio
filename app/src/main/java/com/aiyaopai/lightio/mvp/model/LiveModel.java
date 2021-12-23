package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.bean.UploadTokenBean;
import com.aiyaopai.lightio.mvp.contract.LiveContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LiveModel implements LiveContract.Model {

    @Override
    public Observable<UploadTokenBean> getUpLoadToken(String albumId) {
        TimeZone tz = TimeZone.getDefault();
        int timezoneOffset = (tz.getRawOffset()) / (3600 * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("albumId",albumId);
        map.put("timezoneOffset",timezoneOffset);
        Gson gson = new Gson();
        String strEntity = gson.toJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        return RetrofitClient.getServer().getQiNiuToken(body);
    }


    @Override
    public List<PicBean> getDataList(List<PicBean> dataList) {
        return dataList;
    }
}
