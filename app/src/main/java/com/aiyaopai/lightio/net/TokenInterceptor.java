package com.aiyaopai.lightio.net;

import android.text.TextUtils;

import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.UiUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.aiyaopai.lightio.net.RetrofitClient.getVersion;

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response;
        if (isTokenExpired()) {
            //根据RefreshToken同步请求，获取最新的Token
            String newToken = getNewToken();
            MyLog.show("newToken===" + newToken);
            //使用新的Token，创建新的请求
            Request newRequest = chain.request()
                    .newBuilder()
                    .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()))
                    .addHeader("Authorization", "Bearer " + newToken)
                    .build();
            MyLog.show("newToken===111==" + newToken);
            response = chain.proceed(newRequest);

        } else {
            String token = SPUtils.getString(Contents.access_token);
            Request.Builder builder = chain.request().newBuilder()
                    .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()));
            if (!TextUtils.isEmpty(token)) {
                builder.addHeader("Authorization", "Bearer " + token);
            }
            Request request = builder.build();
            response = chain.proceed(request);
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
            }

        return newToken[0];
    }

    /**
     * 根据Response，判断Token是否失效
     */
    private boolean isTokenExpired() {
        long tokenBeginAt = SPUtils.getLong(Contents.tokenBeginAt);
        long tokenEndAt = System.currentTimeMillis();
        long l = tokenEndAt - tokenBeginAt;
        if ((int) (l / 1000) > 7200 && (int) (l / 1000) < 2590000) {
            MyLog.show("Token 过期了");
            SPUtils.save(Contents.tokenBeginAt, System.currentTimeMillis());//进去一次
            return true;
        }
        return false;
    }

}
