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
    public Observable<SignInBean> loginCode(String phoneNo, String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Contents.client_id, "web");
        map.put(Contents.grant_type, "sms");
        map.put(Contents.CountryCode, 86);
        map.put(Contents.PhoneNo,phoneNo);
        map.put(Contents.code, code);
       return RetrofitClient.getServer().loginCode(map);

    }

    @Override
    public Observable<BaseBean> getCode(String phoneNo) {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.PhoneNo, phoneNo);
        map.put(Contents.CountryCode, "86");
        return RetrofitClient.getServer().sendCode(map);
    }

}
