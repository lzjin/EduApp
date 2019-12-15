package com.danqiu.online.edu.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 下载进度拦截器
 */
public class XProgressInterceptor implements Interceptor {
    private XDownloadProgress listener;
    public XProgressInterceptor(XDownloadProgress listener){
        this.listener = listener;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        //断点续传
//        Request request = chain.request().newBuilder().addHeader("Range" , "bytes=" + startpos + "-").build();
//        Response response = chain.proceed(request);
//        return response.newBuilder()
//                .body(new JsResponseBody(originalResponse.body(), listener))
//                .build();

        Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .body(new XProgressResponseBody(response.body(), listener))
                .build();


    }
}
