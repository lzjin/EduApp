package com.danqiu.online.edu.xupdate;

import android.os.Handler;

import com.danqiu.online.edu.config.MyApplication;
import com.danqiu.online.edu.retrofit.ApiService;
import com.danqiu.online.edu.retrofit.RetrofitManager;
import com.danqiu.online.edu.retrofit.XDownloadProgress;
import com.danqiu.online.edu.retrofit.XProgressInterceptor;
import com.danqiu.online.edu.utils.L;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.util.Map;
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
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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

        // 方式一 网络库
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                        L.i("test", "-----------------APP下载进度: progress=" + progress+"-------------total="+total);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        callback.onError(e);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onStart();
                    }
                });

//方法四
//                httpRxCallback.getDownloadApp(url)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//                        L.e("test", "-----------------onCompleted=" +Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callback.onError(e);
//                        L.e("test", "-----------------onError=" +Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody response) {
//                        L.e("test", "-----------------onNext=" +Thread.currentThread().getName());
//                        File file = FileUtil.createFile(path, fileName);
//                        InputStream is = null;
//                        FileOutputStream os = null;
//                        byte[] buf = new byte[2048];
//                        int len = 0;
//                        try {
//                            is = response.byteStream();
//                            os = new FileOutputStream(file);
//                            total = response.contentLength();
//                            sum = 0;
//                           // callback.onStart();
//                            mainHandler.post(new Runnable() {
//                             @Override
//                             public void run() {
//                                 callback.onStart();
//                             }
//                            });
//                            while ((len = is.read(buf)) != -1) {
//                                os.write(buf, 0, len);
//                                sum += len;
//                                int progress = Math.round(sum * 1F / total * 100F);
//                                callback.onProgress(progress * 1F / 100, total);  //下载中更新进度条
//                                mainHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        callback.onStart();
//                                        callback.onProgress(progress * 1F / 100F,total);
//                                    }
//                                });
//                                L.i("test", "-----------------onProgress=" +progress);
//                            }
//                            os.flush();
//                            callback.onSuccess(file);   //下载完成
//                            L.i("test", "下载完成,安装启动。" + path + "/" + fileName);
//                        } catch (Exception e) {
//                            callback.onError(e);
//                        } finally {
//                            try {
//                                if (is != null) {
//                                    is.close();
//                                }
//                                if (os != null) {
//                                    os.close();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });


// 方式三 自定义进度条


//方法3
//        File file = FileUtil.createFile(path, fileName);
//        XDownloadProgress listener=new XDownloadProgress() {
//            @Override
//            public void onStartDownload() {
//                callback.onStart();
//                L.e("test", "----------------APP下载开始 ");
//            }
//
//            @Override
//            public void onProgress(int progress, long total) {
//                L.e("test", "-----------------onCompleted=" +Thread.currentThread().getName());
//                L.e("test", "-----------------APP下载进度: " + progress);
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callback.onProgress(progress * 1F / 100F,total);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFinishDownload() {
//                L.e("test", "----------------APP下载完成 ");
//                callback.onSuccess(file);
//            }
//
//            @Override
//            public void onFail(Throwable throwable) {
//                callback.onError(throwable);
//                L.e("test", "----------------APP下载失败: " +throwable.getMessage());
//            }
//        };
//
//        httpRxCallback(listener)
//                .getDownloadApp(url)
//                .unsubscribeOn(Schedulers.io())
//                .map(new Func1<ResponseBody, InputStream>() {
//                    @Override
//                    public InputStream call(ResponseBody responseBody) {
//                        return responseBody.byteStream();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Action1<InputStream>() {
//                    @Override
//                    public void call(InputStream inputStream) {
//                        L.e("test", "----------------inputStream ");
//                       // FileUtil.ioWriteFile(inputStream,file);
//                        try {
//                            mainHandler.post(new Runnable() {
//                             @Override
//                             public void run() {
//                                callback.onStart();
//                             }
//                            });
//                           // callback.onStart();
//                            FileUtil.writeFile(inputStream ,file);
//                        }
//                        catch (IOException e) {
//                            e.printStackTrace();
//                            L.e("test", "----------------inputStream--IOException ");
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io())  // 被观察者的实现线程
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())  // 观察者的实现线程
//                .subscribe(new Subscriber() {
//                    @Override
//                    public void onCompleted() {
//                        L.e("test", "-----------------onCompleted=" +Thread.currentThread().getName());
//                        if(file.exists()){
//                           listener.onFinishDownload();
//                        }
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        L.e("test", "-------x--------onError");
//                        listener.onFail(e);
//                    }
//                    @Override
//                    public void onNext(Object o) {
//                    }
//                });

// 方式二 okhttp
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//        //异步请求
//        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                callback.onError(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                InputStream is = null;
//                FileOutputStream os = null;
//                byte[] buf = new byte[2048];
//                int len = 0;
//
//                File file = FileUtil.createFile(path, fileName);
//                try {
//                    is = response.body().byteStream();
//                    os = new FileOutputStream(file);
//                    total = response.body().contentLength();
//                    sum = 0;
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            callback.onStart();
//                        }
//                    });
//                    while ((len = is.read(buf)) != -1) {
//                        os.write(buf, 0, len);
//                        sum += len;
//                        int progress = Math.round(sum * 1F / total * 100F);
//                        mainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callback.onProgress(progress * 1F / 100, total);  //下载中更新进度条
//                                //L.i("test", "-----------------onProgress=" +progress);
//                            }
//                        });
//                    }
//                    os.flush();
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            callback.onSuccess(file);   //下载完成
//                        }
//                    });
//                    L.i("test", "下载完成,安装启动。" + path + "/" + fileName);
//                } catch (Exception e) {
//                    callback.onError(e);
//                } finally {
//                    try {
//                        if (is != null) {
//                            is.close();
//                        }
//                        if (os != null) {
//                            os.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });



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
