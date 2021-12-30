package com.aiyaopai.lightio.net.qiniu;


import android.text.TextUtils;

import com.aiyaopai.lightio.base.CommonObserver;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.bean.UploadFileBean;
import com.aiyaopai.lightio.bean.UploadTokenBean;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.FilesUtil;
import com.aiyaopai.lightio.util.MyLog;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * ClassName: RetrieveImageSubscribe
 * Description:
 * Author: levi
 * CreateDate: 2020/8/13 14:48
 */
public class CeshiSubscribe implements ObservableOnSubscribe<PicBean> {

    private PicBean mPicBean;

    public CeshiSubscribe(PicBean picBean) {
        this.mPicBean = picBean;
    }

    @Override
    public void subscribe(ObservableEmitter<PicBean> emitter) throws Exception {
        String token = mPicBean.getToken();
        File file = new File(mPicBean.getPicPath());
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/jpeg"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody key = RequestBody.create(token, MediaType.parse("text/plain"));

        RetrofitClient.getServer().getUpLoad(key, part);

    }

}