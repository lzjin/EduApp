package com.danqiu.online.edu.xupdate;

import android.os.Handler;
import android.util.Log;

import com.danqiu.online.edu.config.MyApplication;
import com.danqiu.online.edu.retrofit.ApiService;
import com.danqiu.online.edu.retrofit.RetrofitManager;
import com.danqiu.online.edu.retrofit.XDownloadProgress;
import com.danqiu.online.edu.retrofit.XProgressInterceptor;
import com.danqiu.online.edu.utils.FileUtil;
import com.danqiu.online.edu.utils.L;
import com.danqiu.online.edu.utils.SystemUtil;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lzj on 2019/11/11
 * Describe ：自定义网络请求协议,更新进度UI的方法必须切换为Main线程
 */
public class MyUpdateHttpService implements IUpdateHttpService {
    private ApiService httpRxCallback = RetrofitManager.getInstance().apiService;
    private long total;
    private long sum;
    Handler mainHandler = new Handler(MyApplication.app.getMainLooper());
    public MyUpdateHttpService() {

    }

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, @NonNull Callback callBack) {

        String paramKey = (String) params.get("paramKey");
        httpRxCallback.getVersionCode(paramKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            callBack.onSuccess(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, @NonNull Callback callBack) {
        String paramKey = (String) params.get("paramKey");
        httpRxCallback.getVersionCode(paramKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            callBack.onSuccess(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull DownloadCallback callback) {
//        File file = FileUtil.createFile(path, fileName);
//        XDownloadProgress listener=new XDownloadProgress() {
//            @Override
//            public void onStartDownload() {
//                callback.onStart();
//                Log.i("test", "----------------APP下载开始 ");
//            }
//
//            @Override
//            public void onProgress(int progress, long total) {
//                L.i("test", "-----------------onCompleted=" +Thread.currentThread().getName());
//                Log.i("test", "-----------------APP下载进度: " + progress);
//                callback.onProgress(progress * 1F /   100F,total);
//            }
//
//            @Override
//            public void onFinishDownload() {
//                Log.i("test", "----------------APP下载完成 ");
//                callback.onSuccess(file);
//            }
//
//            @Override
//            public void onFail(Throwable throwable) {
//                callback.onError(throwable);
//                Log.i("test", "----------------APP下载失败: " +throwable.getMessage());
//            }
//        };
//
//        httpRxCallback(listener)
//                .getDownloadApp(url)
//                .map(new Func1<ResponseBody, InputStream>() {
//                    @Override
//                    public InputStream call(ResponseBody responseBody) {
//                        return responseBody.byteStream();
//                    }
//                })
//                .subscribeOn(Schedulers.io())// 被观察者的实现线程
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Action1<InputStream>() {
//                    @Override
//                    public void call(InputStream inputStream) {
//                        try {
//                            //callback.onStart();
//                            FileUtil.writeFile(inputStream ,file);
//                        }
//                        catch (IOException e) {
//                            e.printStackTrace();
//                            try {
//                                throw new Exception(e.getMessage(), e);
//                            } catch (Exception e1) {
//                                e1.printStackTrace();
//                            }
//                        }
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())  // 观察者的实现线程
//                .subscribe(new Subscriber() {
//                    @Override
//                    public void onCompleted() {
//                        L.i("test", "-----------------onCompleted=" +Thread.currentThread().getName());
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        listener.onFail(e);
//                    }
//                    @Override
//                    public void onNext(Object o) {
//                        listener.onFinishDownload();
//                    }
//                });


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        //异步请求
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = null;
                FileOutputStream os = null;
                byte[] buf = new byte[2048];
                int len = 0;

                File file = FileUtil.createFile(path, fileName);
                try {
                    is = response.body().byteStream();
                    os = new FileOutputStream(file);
                    total = response.body().contentLength();
                    sum = 0;
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onStart();
                        }
                    });
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                        sum += len;
                        int progress = Math.round(sum * 1F / total * 100F);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onProgress(progress * 1F / 100, total);  //下载中更新进度条
                                //L.i("test", "-----------------onProgress=" +progress);
                            }
                        });
                    }
                    os.flush();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(file);   //下载完成
                        }
                    });
                    L.i("test", "下载完成,安装启动。" + path + "/" + fileName);
                } catch (Exception e) {
                    callback.onError(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        OkHttpUtils.get()
//                .url(url)
//                .build()
//                .execute(new FileCallBack(path, fileName) {
//                    @Override
//                    public void inProgress(float progress, long total, int id) {
//                        L.i("test", "-----------------inProgress" +Thread.currentThread().getName());
//                        callback.onProgress(progress, total);
//                        L.i("test", "-----------------APP下载进度: progress=" + progress+"-------------total="+total);
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e, int id) {
//                        L.i("test", "-----------------onError" +Thread.currentThread().getName());
//                        callback.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(File response, int id) {
//                        L.i("test", "-----------------onResponse" +Thread.currentThread().getName());
//                        callback.onSuccess(response);
//                    }
//
//                    @Override
//                    public void onBefore(Request request, int id) {
//                        L.i("test", "-----------------onBefore" +Thread.currentThread().getName());
//                        super.onBefore(request, id);
//                        callback.onStart();
//                    }
//                });

    }

    @Override
    public void cancelDownload(@NonNull String url) {
        L.i("testz", "取消更新");
    }




    public ApiService httpRxCallback (XDownloadProgress progressListener) {
        OkHttpClient.Builder  okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(5, TimeUnit.SECONDS);
        okHttpClient.readTimeout(5, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(5, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new XProgressInterceptor(progressListener));//自定义下载拦截器

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.Base_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //apiService = retrofit.create(ApiService.class);
        return retrofit.create(ApiService.class);
    }

}
