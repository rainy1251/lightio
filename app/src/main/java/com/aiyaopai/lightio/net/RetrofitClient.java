package com.aiyaopai.lightio.net;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.aiyaopai.lightio.BuildConfig;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.UiUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static volatile RetrofitClient instance;
    private APIService apiService;
    private APIService apiService2;
    private static final boolean isDebug = BuildConfig.DEBUG;
    private final String baseUrl =isDebug ? dev_url:release_url;
    private static final String dev_url = "https://api-sta.devops.back.aiyaopai.com";
    private static final String release_url = "https://api-sta.devops.back.aiyaopai.com";
    private Retrofit retrofit;
    private Retrofit retrofit2;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {
    }

    private static OnErrorCalBackListener mOnErrorCalBackListener;

    public static void setOnErrorCalBackListener(OnErrorCalBackListener onErrorCalBackListener) {
        mOnErrorCalBackListener = onErrorCalBackListener;
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    /**
     * 设置Header
     *
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                .addHeader("User-Agent", "LightIO/Android "+getVersion(UiUtils.getContext()));
                String token = SPUtils.getString(Contents.access_token);
                if (!TextUtils.isEmpty(token)) {
                    requestBuilder.addHeader("Authorization", "Bearer " + token);
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

    }

    /**
     * 设置拦截器 打印日志
     *
     * @return
     */
    private Interceptor getInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    public OkHttpClient getOkHttpClient() {
        final long timeout = 60;//超时时长
        final TimeUnit timeUnit = TimeUnit.SECONDS;//单位秒
        if (okHttpClient == null) {
            //如果为DEBUG 就打印日志
            if (BuildConfig.DEBUG) {

                okHttpClient = new OkHttpClient().newBuilder()

                        //设置Header
                        //.addInterceptor(new TokenInterceptor())
                        .addInterceptor(getHeaderInterceptor())
                        .addInterceptor(errorCodeInterceptor())
                        .connectTimeout(timeout, timeUnit)
                        .addNetworkInterceptor(getInterceptor())
                        .build();

            } else {
                okHttpClient = new OkHttpClient().newBuilder()
                        //设置Header
                        .addInterceptor(new TokenInterceptor())
                        .addInterceptor(errorCodeInterceptor())
                        .connectTimeout(timeout, timeUnit)
                        .addNetworkInterceptor(getInterceptor())
                        .build();
            }

        }

        return okHttpClient;
    }

    public APIService getApi() {
        //初始化一个client,不然retrofit会自己默认添加一个
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //设置网络请求的Url地址
                    .baseUrl(baseUrl)
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        //创建—— 网络请求接口—— 实例
        if (apiService == null) {
            apiService = retrofit.create(APIService.class);
        }

        return apiService;
    }

    public APIService getApi2() {
        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(Contents.ForeGround_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        if (apiService2 == null) {
            apiService2 = retrofit2.create(APIService.class);
        }

        return apiService2;
    }

    /**
     * * 获取版本号
     * * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return  info.versionName;
    }

    public static APIService getServer() {

        return getInstance().getApi();
    }

    public static APIService getServer2() {

        return getInstance().getApi2();
    }

    public static Interceptor errorCodeInterceptor() {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            Buffer buffer = response.body().source().buffer();
            Charset charset = UTF8;
            MediaType contentType = response.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            long contentLength = response.body().contentLength();
            if (contentLength != 0) {
                String msg = buffer.clone().readString(charset);
                mOnErrorCalBackListener.onError(response.code(), msg);
            } else {
                mOnErrorCalBackListener.onError(response.code(), "");
            }
            return response;
        };
    }
}
