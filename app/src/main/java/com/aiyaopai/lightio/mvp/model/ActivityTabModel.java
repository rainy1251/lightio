package com.aiyaopai.lightio.mvp.model;

import android.database.DatabaseUtils;

import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.mvp.contract.ActivityTabContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.DateFormatUtils;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.UiUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class ActivityTabModel implements ActivityTabContract.Model {
    @Override
    public Observable<ActivityListBean> getList(int pageIndex,String type) {
        long currentTimeMillis = System.currentTimeMillis();
        String userId = SPUtils.getString(Contents.Id);
        String currentDate = DateFormatUtils.formatMils(currentTimeMillis);
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Activity.Search");
        map.put(Contents.Fields, "Id,Title,BeginAt,EndAt,CityName,Address,BannerImage,Tags.Name");
        map.put(Contents.PageIndex, pageIndex);
        map.put(Contents.PageSize, "10");
        map.put(Contents.Deleted, false);
        map.put("PhotographerId", userId);

        if (type.equals("进行中")) {
            map.put("BeginAtEnd", currentDate);
            map.put("EndAtBegin", currentDate);

        } else if (type.equals("未开始")) {

            map.put("BeginAtBegin", currentDate);

        } else if (type.equals("已完成")) {

            map.put("EndAtEnd", currentDate);

        }
        return RetrofitClient.getServer().getActivityList(map);
    }
}
