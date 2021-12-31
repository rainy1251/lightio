package com.aiyaopai.lightio.net;

import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.UiUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.aiyaopai.lightio.net.RetrofitClient.getVersion;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (isTokenExpired(response)) {//根据和服务端的约定判断token过期
            MyLog.show("静默自动刷新Token,然后重新请求数据");
            //同步请求方式，获取最新的Token

            //使用新的Token，创建新的请求
            String newToken = getNewToken();
            Request newRequest = chain.request().newBuilder()
                    .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()))
                    .addHeader("Authorization", "Bearer " + newToken)
                    .build();
            return chain.proceed(newRequest);
        }
//        String token = SPUtils.getString(Contents.access_token);
//        Request request = chain.request().newBuilder()
//                .addHeader("User-Agent", "LightIO/Android " + getVersion(UiUtils.getContext()))
//                .addHeader("Authorization", "Bearer " + token)
//                .build();
        return response;

    }

    private boolean isTokenExpired(Response response) {
        if (response.code() == 401) {
            return true;
        }
        return false;
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */

    private String getNewToken() {
        String refreshToken = SPUtils.getString(Contents.refresh_token);

        Call<ResponseBody> call = RetrofitClient.getServer().refreshToken("web", "refresh_token", refreshToken);
//        new Thread(new Runnable() { // 子线程执行
//            @Override
//            public void run() {
//
        try {
            // 4. call对象执行同步请求，请求数据在子线程中执行
            ResponseBody body = call.execute().body();
            Gson gson = new Gson();
            SignInBean signInBean = gson.fromJson(body.toString(), SignInBean.class);

            SPUtils.save(Contents.access_token, signInBean.getAccess_token());
            SPUtils.save(Contents.refresh_token, signInBean.getRefresh_token());
            SPUtils.save(Contents.tokenBeginAt, signInBean.getExpires_in());

        } catch (IOException e) {
            e.printStackTrace();
        }


//            }
//        }).start();

        return "";
    }

    /**
     * 根据Response，判断Token是否失效
     */
//    private boolean isTokenExpired() {
//        long tokenBeginAt = SPUtils.getLong(Contents.tokenBeginAt);
//        long tokenEndAt = System.currentTimeMillis();
//      //  MyLog.show(tokenEndAt-tokenBeginAt + "===");
//        long l = tokenEndAt - tokenBeginAt;
//        if ((int) (l / 1000) > 7200 && (int) (l / 1000) < 2590000) {
//            MyLog.show("Token 过期了");
//            return true;
//        }
//        return false;
//    }

}
