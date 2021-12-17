package com.aiyaopai.lightio.util;

import com.aiyaopai.lightio.BuildConfig;

public class ApiUtils {


    private static final boolean DEBUG = BuildConfig.DEBUG;

   // public static final String BASIC_URL = "https://livemanageapiv2.dev.aiyaopai.com";
   public static final String BASIC_URL = "https://livemanageapiv2.aiyaopai.com";

    public static final String ActivitySearch = "Activity.Search";
    public static final String AccountSignIn = "Account.SignIn";
    public static final String SignIM = "Activity.IMSign";
    public static final String SignInWithPhoneNo= "Account.SignInWithPhoneNo";
    public static final String SignInWithPhoneNoVerify= "Account.SignInWithPhoneNoVerify";//验证码验证管理
    public static final String Banner = "Advertisement.EquipmentAllWithNoToken";//首页轮播

    public static final String Home_Class_One = "https://www.aiyaopai.com/";//邀拍云摄影
    public static final String Home_Class_Two = "https://help.aiyaopai.com/DocManage/5c1794913eb3303d6086bcbd";//帮助中心
    public static final String Home_Class_Three = "https://live.aiyaopai.com/static/order_v2/index.html?fromActivity=5970050";//预约云摄影
    public static final String Home_Class_Four = "https://www.aiyaopai.com/recruit";//招募摄影师
}