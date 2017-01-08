package io.sixth.glassbook.data.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
 * Created by Jorik on 08/01/2017.
 */

public class httpClientAPI {
    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
    }
}
