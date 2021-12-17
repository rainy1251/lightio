package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.BannerBean;
import com.aiyaopai.lightio.mvp.contract.HomeContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.ApiUtils;
import com.aiyaopai.lightio.util.Contents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class HomeModel implements HomeContract.Model {

    @Override
    public Observable<List<BannerBean>> banner() {
        String Fields = "Id,Title,Link,Cover";
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api,ApiUtils.Banner);
        map.put(Contents.Fields,Fields);
        return RetrofitClient.getServer().banner(map);
    }
}
