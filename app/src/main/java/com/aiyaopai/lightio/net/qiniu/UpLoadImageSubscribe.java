package com.aiyaopai.lightio.net.qiniu;


import android.text.TextUtils;

import com.aiyaopai.lightio.base.CommonObserver;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.bean.UploadFileBean;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.FilesUtil;
import com.aiyaopai.lightio.util.MyLog;

import java.io.File;

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
public class UpLoadImageSubscribe implements ObservableOnSubscribe<PicBean> {

    private String mPicPath;
    private PicBean mPicBean;

    public UpLoadImageSubscribe(PicBean picBean) {
        this.mPicPath = picBean.getPicPath();
        this.mPicBean = picBean;
    }

    @Override
    public void subscribe(ObservableEmitter<PicBean> emitter) throws Exception {

        String token = mPicBean.getToken();
        File file = new File(mPicBean.getPicPath());
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/jpeg"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody key = RequestBody.create(token, MediaType.parse("text/plain"));

        RetrofitClient.getServer().getUpLoad(key, part)
                .subscribe(new CommonObserver<UploadFileBean>() {
                    @Override
                    public void onNext(UploadFileBean uploadFileBean) {

                        if (uploadFileBean.getResult().getSize() > 0) {
                            mPicBean.setToken("成功");
                            mPicBean.setProgress(100);
                            mPicBean.setStatus(1);
                            emitter.onNext(mPicBean);
                            emitter.onComplete();
                            if (!TextUtils.isEmpty(uploadFileBean.getCallbackResult().getUrl())) {
                                mPicBean.setPicPath(uploadFileBean.getCallbackResult().getUrl());
                            }
                            FilesUtil.deleteFile(mPicPath);
                        } else {
                            mPicBean.setToken("失败");
                            mPicBean.setProgress(0);
                            mPicBean.setStatus(-1);
                            emitter.onNext(mPicBean);
                            emitter.onComplete();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        MyLog.show(e.toString());
                    }
                });

    }

}