package com.aiyaopai.lightio.net;

import android.text.TextUtils;

import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.components.activity.LoginActivity;
import com.aiyaopai.lightio.event.LoginAgainEvent;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

import static com.aiyaopai.lightio.net.RetrofitClient.getVersion;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SPUtils.getString(Contents.access_token);
        Request request = chain.request().newBuilder()
                .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()))
                .addHeader("Authorization", "Bearer " + token).build();
        Response response = chain.proceed(request);

        if (isTokenExpired(response)) {
            //根据RefreshToken同步请求，获取最新的Token
            String newToken = getNewToken();
            if (!TextUtils.isEmpty(newToken)) {
                Request newRequest = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()))
                        .addHeader("Authorization", "Bearer " + newToken)
                        .build();
                response = chain.proceed(newRequest);
            } else {
                EventBus.getDefault().post(new LoginAgainEvent());
            }

        }
        return response;
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return newToken
     */
    private String getNewToken() {
        final String[] newToken = {""};
        String refreshToken = SPUtils.getString(Contents.refresh_token);
        if (!TextUtils.isEmpty(refreshToken)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(Contents.client_id, "web");
            map.put(Contents.grant_type, "refresh_token");
            map.put(Contents.refresh_token, refreshToken);

            Call<SignInBean> signInBeanCall = RetrofitClient.getServer().refreshToken(map);
            SignInBean body = null;
            try {
                body = signInBeanCall.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (body != null) {
                newToken[0] = body.getAccess_token();
                SPUtils.save(Contents.access_token, body.getAccess_token());
                SPUtils.save(Contents.refresh_token, body.getRefresh_token());
                SPUtils.save(Contents.tokenBeginAt, System.currentTimeMillis());
            } else {
                MyLog.show("重新登录1");
                EventBus.getDefault().post(new LoginAgainEvent());
            }
        }
        return newToken[0];
    }


    private boolean isTokenExpired(Response response) {
        if (response.code() == 401) {
            MyLog.show("Token 过期了");
            SPUtils.save(Contents.access_token, "");
            return true;
        }
        return false;
    }

}
