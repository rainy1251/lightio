package com.aiyaopai.lightio.net.qiniu;


import android.text.TextUtils;

import com.aiyaopai.lightio.base.CommonObserver;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.bean.UploadTokenBean;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * ClassName: RetrieveImageSubscribe
 * Description:
 * Author: levi
 * CreateDate: 2020/8/13 14:48
 */
public class UploadTokenSubscribe implements ObservableOnSubscribe<PicBean> {

    private String mAlbumId;
    private PicBean mPicBean;

    public UploadTokenSubscribe(PicBean picBean, String albumId) {
        this.mPicBean = picBean;
        this.mAlbumId = albumId;
    }

    @Override
    public void subscribe(ObservableEmitter<PicBean> emitter) throws Exception {

        TimeZone tz = TimeZone.getDefault();
        int timezoneOffset = (tz.getRawOffset()) / (3600 * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("albumId", mAlbumId);
        map.put("timezoneOffset", timezoneOffset);
        Gson gson = new Gson();
        String strEntity = gson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json"));

        RetrofitClient.getServer()
                .getQiNiuToken(body)
                .subscribe(new CommonObserver<UploadTokenBean>() {
                    @Override
                    public void onNext(UploadTokenBean tokenBean) {
                        String upToken = tokenBean.getResult().getToken();
                        if (!TextUtils.isEmpty(upToken)) {
                            mPicBean.setToken(upToken);
                            emitter.onNext(mPicBean);
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

}