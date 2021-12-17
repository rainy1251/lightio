package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.mvp.contract.LiveContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class LiveModel implements LiveContract.Model {

    @Override
    public List<PicBean> getDataList(List<PicBean> dataList) {
        return dataList;
    }
}
