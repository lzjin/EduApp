package com.danqiu.online.edu.retrofit;

import com.danqiu.online.edu.utils.L;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lzj on 2019/10/22
 * Describe ：Retrofit
 */
public class RetrofitManager {
    public  static String baseUrl = ApiService.Base_URL;
    private static final int DEFAULT_TIMEOUT = 5;
    private static RetrofitManager mRetrofit;
    public   ApiService apiService;
    public   OkHttpClient.Builder  okHttpClient;

    public static RetrofitManager getInstance() {
        if(mRetrofit==null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofit == null)
                    mRetrofit = new RetrofitManager();
            }
        }
        baseUrl = ApiService.Base_URL;
        return mRetrofit;
    }

    public static RetrofitManager getInstance(String base_Url) {
        if(mRetrofit==null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofit == null)
                    mRetrofit = new RetrofitManager();
            }
        }
        baseUrl = base_Url;
        return mRetrofit;
    }


    private RetrofitManager() {
        okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if(L.isShowLogo){
            okHttpClient.addInterceptor(new LoggingInterceptor());//自定义拦截器
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(ApiService.class);
    }
}
