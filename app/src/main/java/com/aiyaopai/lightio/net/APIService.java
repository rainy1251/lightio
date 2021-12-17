package com.aiyaopai.lightio.net;


import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.bean.BannerBean;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    /**
     * 首页banner
     */
    @FormUrlEncoded
    @POST(".")
    Observable<List<BannerBean>> banner(@FieldMap Map<String, Object> map);

    /**
     * 活动列表
     */
    @FormUrlEncoded
    @POST(".")
    Observable<ActivityListBean> activitySearch(@FieldMap Map<String, Object> map);

    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST(".")
    Observable<SignInBean> getUserInfo(@FieldMap Map<String, Object> map);

    /**
     * 手机号密码登录
     */
    @FormUrlEncoded
    @POST(".")
    Observable<SignInBean> loginPwd(@FieldMap Map<String, Object> map);

    /**
     * 验证码登录
     */
    @FormUrlEncoded
    @POST(".")
    Observable<SignInBean> loginCode(@FieldMap Map<String, Object> map);

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST(".")
    Observable<BaseBean> signOutApi(@FieldMap Map<String, Object> map);

    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST(".")
    Observable<BaseBean> resetPwd(@FieldMap Map<String, Object> map);

    /**
     * 发送验证码
     */
    @FormUrlEncoded
    @POST(".")
    Observable<BaseBean> sendCode(@FieldMap Map<String, Object> map);

    /**
     * 获取活动列表
     */
    @FormUrlEncoded
    @POST(".")
    Observable<ActivityListBean> getActivityList(@FieldMap Map<String, Object> map);

    /**
     * 获取图片上传令牌
     */
    @FormUrlEncoded
    @POST(".")
    Observable<BaseBean> getQiNiuToken(@FieldMap Map<String, Object> map);

    /**
     * 查找原始照片列表
     */
    @FormUrlEncoded
    @POST(".")
    Observable<ActivityListBean> getOriginalPic(@FieldMap Map<String, Object> map);
}
