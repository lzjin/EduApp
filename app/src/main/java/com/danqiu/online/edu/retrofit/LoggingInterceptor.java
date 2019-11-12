package com.danqiu.online.edu.retrofit;

import com.danqiu.online.edu.utils.L;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static okhttp3.internal.http.StatusLine.HTTP_CONTINUE;

/**
 * Created by lzj on 2019/10/22
 * Describe ：拦截日志
 */
public class LoggingInterceptor implements Interceptor {
    private String TAG="test";
    private final Charset UTF8 = Charset.forName("UTF-8");
    @Override
    public Response intercept(Chain chain) throws IOException {

        //请求-------------------------------------------
        Request request = chain.request();
        RequestBody requestBody = request.body();

//        Request request = chain.request();
//        Request.Builder requestBuilder = request.newBuilder();
//        requestBuilder.addHeader("Authorization", "11111"); //添加默认的Token请求头
//        RequestBody requestBody =request.body();

        String body = null;

        if(requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            body = buffer.readString(charset);
        }

        L.e(TAG, "-------请求地址="+request.url()+"-----body="+body+"----header="+request.headers());


        //响应-------------------------------------------
        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        String rBody = null;

        if(hasBody(request,response)) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            rBody = buffer.clone().readString(charset);
        }

        L.e(TAG, "-------接收Code="+response.code()+"------json="+rBody);
        //MLog.i(TAG, "-------接收-------信息="+response.message()+"");
        // MLog.i(TAG,"--------接收------tookMs="+tookMs+"");

        return response;
    }


    private boolean hasBody(Request request, Response response) {
        // HEAD requests never yield a body regardless of the response headers.
        if (response.request().method().equals("HEAD")) {
            return false;
        }

        int responseCode = response.code();
        if ((responseCode < HTTP_CONTINUE || responseCode >= 200)
                && responseCode != HTTP_NO_CONTENT
                && responseCode != HTTP_NOT_MODIFIED) {
            return true;
        }

        if (stringToLong(request.headers().get("Content-Length")) != -1
                || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return true;
        }

        return false;
    }

    private long stringToLong(String s) {
        if (s == null) return -1;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
