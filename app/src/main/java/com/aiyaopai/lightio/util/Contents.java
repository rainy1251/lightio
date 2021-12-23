package com.aiyaopai.lightio.util;

import static com.aiyaopai.lightio.BuildConfig.DEBUG;

public class Contents  {

    public static final String Deleted = "Deleted";
    public static final String OfficialSiteEnabled = "OfficialSiteEnabled";

    public static final String Rolecode = "rolecode";


    public static final String Api = "Api";

    public static final String client_id = "client_id";
    public static final String grant_type = "grant_type";
    public static final String Success = "Success";
    public static final String PhoneNo = "phoneNo";
    public static final String CountryCode = "countryCode";
    public static final String code = "code";
    public static final String access_token = "access_token";
    public static final String refresh_token = "refresh_token";
    public static final String tokenBeginAt = "tokenBeginAt";
    public static final String offset = "offset";
    public static final String limit = "limit";
    public static final String roles = "roles";




    public static final String TagNames = "TagNames";
    public static final String ActivityId = "activityId";
    public static final String Total = "total";
    public static final String APP_ID = "wxd3b76a4070d1e98d";  //release
    //    public static final String APP_ID = "wxa453d50368c692ef"; //Debug
    public static final String AppSecret = "15431961474850e0dc9c8313e5ce9d1316021bfbe1bd2";
    //管理，加盟商，用户
    public static final String Manager = "Manager";
    public static final String Franchisee = "Franchisee";
    public static final String Customer = "Customer";
    public static final String Role = "Role";//用户类型





    public static final String Password = "Password";
    public static final String OldPassword = "OldPassword";
    public static final String NewPassword = "NewPassword";
    public static final String Fields = "Fields";
    public static final String PageIndex = "PageIndex";
    public static final String PageSize = "PageSize";
    public static final String BeginAtbegin = "BeginAtbegin";
    public static final String BeginAtEnd = "BeginAtEnd";
    public static final String EndAtEnd = "EndAtEnd";
    public static final String NECaptchaValidate = "NECaptchaValidate";
    public static final String TencentCaptchaValidate = "TencentCaptchaValidate";
    public static final String RandStr = "RandStr";
    public static final String PassWord = "PassWord";

    public static final String BeginAt = "BeginAt";
    public static final String EndAt = "EndAt";

    public static final String Id = "Id";
    public static final String Title = "Title";
    public static final String PrevId = "PrevId";//PrevId
    public static final String Avatar = "Avatar";
    public static final String Nickname = "Nickname";

    public static final String BuglyAppIdDebug = "eeba935b2e";
    public static final String BuglyAppIdRelease = "eeba935b2e";
    public static final String BuglyAppId = DEBUG?BuglyAppIdDebug:BuglyAppIdRelease;


    public static final String QiNiuToken = "QiNiuToken";
    public static final float TopHeight = 25;//沉浸式顶部距离
    public static final int Code_200 =  200	;//请求成功	接口成功执行会返回该状态码
    public static final int Code_201 = 201;//新增成功	新增数据成功会返回该状态码
    public static final int Code_400 =400;//请求失败	请求出现错误, 该错误一般是由于传递的参数错误导致的
    public static final int Code_401 =401;//	未授权	访问的接口需要授权, 即需要登录令牌 Token
    public static final int Code_403 =403;//	禁止访问	一般是由于 CORS 跨域认证失败导致的
    public static final int Code_404 =404;//数据未找到	即请求的数据不存在
    public static final int Code_405 =405;//不支持请求的方法	接口只支持 POST 请求
    public static final int Code_415 =415;//	不支持请求的媒体类型	POST 请求只支持 application/x-www-form-urlencoded
    public static final int Code_500 =500;//后端服务异常	后端服务存在 BUG
    public static final String ForeGround_Release_URL = "https://liveclientapiv2.aiyaopai.com";
    public static final String ForeGround_Debug_URL = "https://liveclientapiv2.dev.aiyaopai.com";
    public static final String ForeGround_URL = DEBUG ? ForeGround_Debug_URL : ForeGround_Release_URL;
    public static final String ACTIVITY_Release_URL = "https://live.aiyaopai.com/live/";
    public static final String ACTIVITY_Debug_URL = "https://live.dev.aiyaopai.com/live/";
    public static final String ACTIVITY_URL = DEBUG ? ACTIVITY_Debug_URL : ACTIVITY_Release_URL;


    public static final String UrlPath = "UrlPath";
    public static final String UrlTitle = "UrlTitle";
    public static final String UPLOAD_MODE = "upload_mode";//上传方式
    public static final String DIRECT_UPLOAD= "direct"; //边拍边传
    public static final String HAND_UPLOAD= "hand";
    public static final String RATING_UPLOAD = "rating";
    public static final String AUTO = "isAutoScan";//是否开启自动扫描
    public static final String VIBRATE= "isVibrate";//是否断开时震动
    public static final String PHOTO_PX= "photoType";//照片分辨率
    public static final String TopSpeed= "TopSpeed";
    public static final String Standard= "Standard";
    public static final String Original= "Original";
    public static final String HelpUrl = "https://help.aiyaopai.com/DocManage/5c1794913eb3303d6086bcbd";

}
