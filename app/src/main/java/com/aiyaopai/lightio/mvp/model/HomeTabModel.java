package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.AlbumListBean;
import com.aiyaopai.lightio.mvp.contract.HomeTabContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.ApiUtils;
import com.aiyaopai.lightio.util.Contents;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;


public  class HomeTabModel implements HomeTabContract.Model {
    @Override
    public Observable<AlbumListBean> activitySearch(int pageIndex, String tagStr) {
        String Fields = "Id,Title,BannerImage,Address,BeginAt";
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, ApiUtils.ActivitySearch);
        map.put(Contents.Fields, Fields);
        map.put(Contents.PageIndex, pageIndex);
        map.put(Contents.PageSize, 20);
        map.put(Contents.Deleted, false);
        map.put(Contents.OfficialSiteEnabled, true);
        if (!tagStr.equals("全部")) {
            map.put(Contents.TagNames, tagStr);
        }
        return RetrofitClient.getServer2().activitySearch(map);
    }
}
