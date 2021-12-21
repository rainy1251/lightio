package com.aiyaopai.lightio.net;


import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.bean.BannerBean;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.OriginalPicBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.bean.UserBean;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
    @GET("/user/current")
    Observable<UserBean> getUserInfo();

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
    @POST("/oauth/connect/token")
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
    @POST("/oauth/smsconnect/code")
    Observable<BaseBean> sendCode(@FieldMap Map<String, Object> map);

    /**
     * 获取活动列表
     */
    @GET("/activity/list")
    Observable<ActivityListBean> getActivityList(@Query("offset") int offset
            , @Query("limit") int limit, @Query("roles") String roles);

    /**
     * 获取图片上传令牌
     */
    @FormUrlEncoded
    @POST("originalpicture/uploadToken")
    Observable<BaseBean> getQiNiuToken(@FieldMap Map<String, Object> map);

    /**
     * 查找原始照片列表
     */
    @GET("/originalpicture/list")
    Observable<OriginalPicBean> getOriginalPic(@Query("offset") int offset, @Query("limit") int limit
            , @Query("albumId") String albumId, @Query("createdUserId") String createdUserId);
}
