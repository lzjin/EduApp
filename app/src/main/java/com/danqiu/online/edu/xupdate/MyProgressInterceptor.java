package com.danqiu.online.edu.xupdate;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by lzj on 2019/11/12
 * Describe ：下载进度拦截器
 */
public class MyProgressInterceptor implements Interceptor {
    private  MyProgressListener listener;

    public MyProgressInterceptor(MyProgressListener listener){
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
                .body(new MyResponseBody(response.body(), listener))
                .build();


    }
}
