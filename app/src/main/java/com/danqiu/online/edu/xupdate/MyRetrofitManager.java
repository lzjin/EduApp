package com.danqiu.online.edu.xupdate;

import com.danqiu.online.edu.retrofit.ApiService;
import com.danqiu.online.edu.utils.FileUtil;
import com.danqiu.online.edu.utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lzj on 2019/11/12
 * Describe ：注释
 */
public class MyRetrofitManager {
    /**
     * 服务器 Url
     */
    public static String baseUrl = ApiService.Base_URL;
    private static final int DEFAULT_TIMEOUT = 5;
    public OkHttpClient.Builder  httpClient;
    public ApiService apiService;
    private Retrofit retrofit;
    private MyProgressListener mListener;


    public MyRetrofitManager(MyProgressListener listener) {
        mListener = listener;

        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new MyProgressInterceptor(mListener));//自定义下载拦截器
        httpClient.retryOnConnectionFailure(true);
        httpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    //下载
    public void downApkFile(String url, final String path, final String fileName, Subscriber subscriber){
        Observable observable = apiService.getDownloadApp(url)
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())    //耗时处理调度
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            FileUtil.writeFile(inputStream , FileUtil.createFile(path,fileName));
                        } catch (IOException e) {
                            L.i("test","文件写入失败");
                            e.printStackTrace();
                        }
                    }
                });
        observable.subscribeOn(Schedulers.io())  // 被观察者的实现线程
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())  // 观察者的实现线程
                .subscribe(subscriber);
    }
}
