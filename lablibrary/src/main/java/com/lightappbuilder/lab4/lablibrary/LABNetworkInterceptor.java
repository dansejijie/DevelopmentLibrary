package com.lightappbuilder.lab4.lablibrary;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yinhf on 2016/11/18.
 */

public class LABNetworkInterceptor implements Interceptor {

    private String userAgent;

    private String getUserAgent() {
        if (userAgent == null) {
            userAgent = Config.userAgent();
        }
        return userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request userRequest = chain.request();
        Request.Builder requestBuilder = userRequest.newBuilder();
        //设置lab User-Agent
        String ua = userRequest.header("User-Agent");
        if (ua == null) {
            ua = getUserAgent();
        } else {
            ua = ua + " " + getUserAgent();
        }
        requestBuilder.header("User-Agent", ua);

        return chain.proceed(requestBuilder.build());
    }
}
