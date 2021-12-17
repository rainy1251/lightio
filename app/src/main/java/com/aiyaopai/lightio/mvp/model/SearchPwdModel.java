package com.aiyaopai.lightio.mvp.model;

import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.mvp.contract.SearchPwdContract;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class SearchPwdModel implements SearchPwdContract.Model {

    @Override
    public Observable<BaseBean> sendCode(String phoneNo ,String randStr, String ticket) {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Account.ResetPasswordWithPhoneNo");
        map.put(Contents.PhoneNo, phoneNo);
        map.put(Contents.Role, "Photographer");
        map.put(Contents.TencentCaptchaValidate, ticket);
        map.put(Contents.RandStr, randStr);
        return RetrofitClient.getServer().sendCode(map);
    }

    @Override
    public Observable<BaseBean> resetPwd(String phoneNo, String code, String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put(Contents.Api, "Account.ResetPasswordWithPhoneNoVerify");
        map.put("Code", code);
        map.put("PassWord", pwd);
        map.put("PhoneNo", phoneNo);
        map.put("Role", "Photographer");
        return RetrofitClient.getServer().resetPwd(map);
    }
}
