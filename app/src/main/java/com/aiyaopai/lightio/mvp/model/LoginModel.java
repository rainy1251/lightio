package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.mvp.contract.LoginContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<SignInBean> loginPwd(String phoneNo, String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Account.SignIn");
        map.put(Contents.PhoneNo, phoneNo);
        map.put(Contents.PassWord, pwd);
        map.put(Contents.Role, "Photographer");
        return RetrofitClient.getServer().loginPwd(map);
    }

    @Override
    public Observable<SignInBean> loginCode(String phoneNo, String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Account.SignInWithPhoneNoVerify");
        map.put(Contents.PhoneNo, phoneNo);
        map.put(Contents.Code, code);
        map.put(Contents.Role, "Photographer");
        return RetrofitClient.getServer().loginCode(map);
    }

    @Override
    public Observable<BaseBean> getCode(String phoneNo, String randStr, String ticket) {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Account.SignInWithPhoneNo");
        map.put(Contents.PhoneNo, phoneNo);
        map.put(Contents.Role, "Photographer");
        map.put(Contents.TencentCaptchaValidate, ticket);
        map.put(Contents.RandStr, randStr);
        return RetrofitClient.getServer().sendCode(map);
    }
}
