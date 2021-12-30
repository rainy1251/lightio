package com.aiyaopai.lightio.net;


import com.aiyaopai.lightio.bean.AlbumListBean;
import com.aiyaopai.lightio.bean.BannerBean;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.OriginalPicBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.bean.UploadFileBean;
import com.aiyaopai.lightio.bean.UploadTokenBean;
import com.aiyaopai.lightio.bean.UserBean;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<AlbumListBean> activitySearch(@FieldMap Map<String, Object> map);

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
     * 刷新令牌
     */
    @FormUrlEncoded
    @POST("/oauth/connect/token")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<ResponseBody> refreshToken(@Field("client_id") String client_id
            , @Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token);
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
     * 获取相册列表
     */
    @GET("/livealbum/list")
    Observable<AlbumListBean> getAlbumList(@Query("offset") int offset
            , @Query("limit") int limit, @Query("roles") String roles, @Query("state") String state);

    /**
     * 获取图片上传令牌
     */
    @POST("originalpicture/uploadToken")
    Observable<UploadTokenBean> getQiNiuToken(@Body RequestBody requestBody);

    /**
     * 上传对象(文件)
     */
    @Multipart
    @POST("/upload")
    Observable<UploadFileBean> getUpLoad(@Part("token") RequestBody token,@Part MultipartBody.Part file);

    /**
     * 查找原始照片列表
     */
    @GET("/originalpicture/list")
    Observable<OriginalPicBean> getOriginalPic(@Query("offset") int offset, @Query("limit") int limit
            , @Query("albumId") String albumId, @Query("createdUserId") String createdUserId);
}
