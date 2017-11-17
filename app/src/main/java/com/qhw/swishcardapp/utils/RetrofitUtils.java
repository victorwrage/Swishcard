package com.qhw.swishcardapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.BuildConfig;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qhw.swishcardapp.AppConstants;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hello_world on 2016/11/24.
 */

public class RetrofitUtils {

    public static Retrofit getRetrofit(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /**
         *设置缓存，代码略
         */
        setCache(context, builder);
        /**
         *  公共参数，代码略
         */
        setPulic(builder);
        /**
         * 设置头，代码略
         */
        setHeaders(builder);
        /**
         * Log信息拦截器，代码略
         */
        setLogInterceptor(builder);
        /**
         * 设置cookie，代码略
         */
        setCookie(builder);
        /**
         * 设置超时和重连，代码略
         */
        setLongConnectionTime(builder);

        //以上设置结束，才能build(),不然设置白搭
        OkHttpClient okHttpClient = builder
                .build();
        return new Retrofit.Builder()
                .baseUrl(AppConstants.URL_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static void setLongConnectionTime(OkHttpClient.Builder builder) {
        builder.connectTimeout(5, TimeUnit.SECONDS);//原本是20
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
    }

    private static void setCookie(OkHttpClient.Builder builder) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
    }

    private static void setHeaders(OkHttpClient.Builder builder) {

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("AppType", "TPOS")
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(headerInterceptor);
    }

    private static void setPulic(OkHttpClient.Builder builder) {
        Interceptor addQueryParameterInterceptor = chain -> {
            Request originalRequest = chain.request();
            Request request;
            String method = originalRequest.method();
            Headers headers = originalRequest.headers();
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
                    .addQueryParameter("platform", "android")
                    .addQueryParameter("version", "1.0.0")
                    .build();
            request = originalRequest.newBuilder().url(modifiedUrl).build();
            return chain.proceed(request);
        };
        builder.addInterceptor(addQueryParameterInterceptor);
    }

    private static void setLogInterceptor(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
    }

    private static void setCache(final Context context, OkHttpClient.Builder builder) {
        File cacheFile = new File(context.getExternalCacheDir(), "retrotCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkConnected(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (isNetworkConnected(context)) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("WuXiaolong")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
        builder.cache(cache).addNetworkInterceptor(cacheInterceptor);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
