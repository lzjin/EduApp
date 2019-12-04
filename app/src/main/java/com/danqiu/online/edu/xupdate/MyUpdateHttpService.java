package com.danqiu.online.edu.xupdate;

import android.os.Handler;

import com.danqiu.online.edu.config.MyApplication;
import com.danqiu.online.edu.retrofit.ApiService;
import com.danqiu.online.edu.retrofit.RetrofitManager;
import com.danqiu.online.edu.utils.FileUtil;
import com.danqiu.online.edu.utils.L;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
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
        File file = FileUtil.createFile(path, fileName);

        httpRxCallback.getDownloadApp(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream >() {

                    @Override
                    public InputStream  call(ResponseBody responseBody) {
                        total = responseBody.contentLength();
                        return responseBody.byteStream();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // 用于计算任务
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream responseBody) {
                       // writeFile(inputStream, file);
                        L.i("test", "-----------------call=" +Thread.currentThread().getName());
                        InputStream is = null;
                        FileOutputStream os = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        try {
                            is = responseBody;
                           // is = responseBody.byteStream();
                            os = new FileOutputStream(file);
                           // total = responseBody.contentLength();
                            sum = 0;
                            callback.onStart();
                            while ((len = is.read(buf)) != -1) {
                                os.write(buf, 0, len);
                                sum += len;
                                int progress = Math.round(sum * 1F / total * 100F);
                                callback.onProgress(progress * 1F / 100, total);  //下载中更新进度条
                                L.i("test", "-------------call----onProgress=" +progress);
                            }
                            os.flush();
                            callback.onSuccess(file);   //下载完成
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
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InputStream>() {
                    @Override
                    public void onCompleted() {
                        L.i("test", "-----------------onCompleted=" +Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                        L.i("test", "-----------------onError=" +Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                        L.i("test", "-----------------onNext=" +Thread.currentThread().getName());
                        callback.onSuccess(file);
                    }
                });


//        httpRxCallback.getDownloadApp(url)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//                        L.i("test", "-----------------onCompleted=" +Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callback.onError(e);
//                        L.i("test", "-----------------onError=" +Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody response) {
//                        L.i("test", "-----------------onNext=" +Thread.currentThread().getName());
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
//                            callback.onStart();
//                            while ((len = is.read(buf)) != -1) {
//                                os.write(buf, 0, len);
//                                sum += len;
//                                int progress = Math.round(sum * 1F / total * 100F);
//                                callback.onProgress(progress * 1F / 100, total);  //下载中更新进度条
//                                //L.i("test", "-----------------onProgress=" +progress);
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

//        httpRxCallback.getDownloadApp(url)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//                        L.i("test", "-----------------onCompleted=" +Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callback.onError(e);
//                        L.i("test", "-----------------onError=" +Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody response) {
//                        L.i("test", "-----------------onNext=" +Thread.currentThread().getName());
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
//                            callback.onStart();
//                            while ((len = is.read(buf)) != -1) {
//                                os.write(buf, 0, len);
//                                sum += len;
//                                int progress = Math.round(sum * 1F / total * 100F);
//                                callback.onProgress(progress * 1F / 100, total);  //下载中更新进度条
//                                //L.i("test", "-----------------onProgress=" +progress);
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




    private Map<String, String> transform(Map<String, Object> params) {
        Map<String, String> map = new TreeMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }
        return map;
    }
}
