package com.demo.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 凡 on 2018/5/31 0031.
 * 网络请求封装
 */

public class HttpUtils {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
