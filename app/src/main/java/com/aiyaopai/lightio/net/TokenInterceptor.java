package com.aiyaopai.lightio.net;

import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.UiUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

import static com.aiyaopai.lightio.net.RetrofitClient.getVersion;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        if (isTokenExpired()) {
            String newToken = getNewToken();
            Request newRequest = chain.request().newBuilder()
                    .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()))
                    .addHeader("Authorization", "Bearer " + newToken)
                    .build();
            return chain.proceed(newRequest);
        }
        String token = SPUtils.getString(Contents.access_token);
        Request request = chain.request().newBuilder()
                .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()))
                .addHeader("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(request);
    }

    private String getNewToken() {
        String refreshToken = SPUtils.getString(Contents.refresh_token);
        Call<SignInBean> call = RetrofitClient.getServer().refreshToken("web", "refresh_token", refreshToken);
        try {
            // 4. call对象执行同步请求，请求数据在子线程中执行
            retrofit2.Response<SignInBean> execute = call.execute();
            SignInBean body = execute.body();
            assert body != null;
            SPUtils.save(Contents.access_token, body.getAccess_token());
            SPUtils.save(Contents.refresh_token, body.getRefresh_token());
            SPUtils.save(Contents.tokenBeginAt,System.currentTimeMillis());
            return body.getAccess_token();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "body.getAccess_token()";
    }

    /**
     * 根据Response，判断Token是否失效
     *
     */
    private boolean isTokenExpired() {
        long tokenBeginAt = SPUtils.getLong(Contents.tokenBeginAt);
        long tokenEndAt = System.currentTimeMillis();
        MyLog.show(tokenBeginAt+"==="+tokenEndAt);
        long l = tokenEndAt - tokenBeginAt;
        if ((int)(l/1000) >7200 && (int)(l/1000)<2590000) {
            MyLog.show("Token 过期了");
            return true;
        }
        return false;
    }

}
