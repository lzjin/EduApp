//package com.danqiu.online.edu.xupdate;
//
//import com.danqiu.online.edu.config.Constants;
//import com.danqiu.online.edu.retrofit.ApiService;
//import com.danqiu.online.edu.retrofit.RetrofitManager;
//import com.danqiu.online.edu.utils.FileUtil;
//import com.danqiu.online.edu.utils.L;
//import com.xuexiang.xupdate.proxy.IUpdateHttpService;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.TreeMap;
//
//import androidx.annotation.NonNull;
//import okhttp3.ResponseBody;
//import rx.Observer;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Created by lzj on 2019/11/11
// * Describe ：自定义网络请求协议
// */
//public class MyUpdateHttpService implements IUpdateHttpService {
//    private ApiService httpRxCallback = RetrofitManager.getInstance().apiService;
//
//    public MyUpdateHttpService() {
//    }
//
//    @Override
//    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, @NonNull Callback callBack) {
//
//        String paramKey = (String) params.get("paramKey");
//        httpRxCallback.getVersionCode(paramKey)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callBack.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//                        try {
//                            callBack.onSuccess(responseBody.string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    @Override
//    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, @NonNull Callback callBack) {
//        String paramKey = (String) params.get("paramKey");
//        httpRxCallback.getVersionCode(paramKey)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callBack.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//                        try {
//                            callBack.onSuccess(responseBody.string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    @Override
//    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull DownloadCallback callback) {
//
////        OkHttpClient okHttpClient = new OkHttpClient();
////        Request request = new Request.Builder().url(url).build();
////        //异步请求
////        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
////            @Override
////            public void onFailure(Call call, IOException e) {
////                callback.onError(e);
////            }
////
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////
////                InputStream is = null;
////                FileOutputStream os = null;
////                byte[] buf = new byte[2048];
////                int len = 0;
////
////                File file = FileUtil.createFile(path, fileName);
////                try {
////                    is = response.body().byteStream();
////                    os = new FileOutputStream(file);
////                    long total = response.body().contentLength();
////                    long sum = 0;
////                    while ((len = is.read(buf)) != -1) {
////                        os.write(buf, 0, len);
////                        sum += len;
////                        int progress = (int) (sum * 1.0f / total * 100);
////                        L.i("test", "-----------------APP下载进度: " + progress);
////                        callback.onProgress(sum, total);  //下载中更新进度条
////                    }
////                    os.flush();
////                    callback.onSuccess(file);   //下载完成
////                    L.i("testz", "下载完成,安装启动。" + path + "/" + fileName);
////                } catch (Exception e) {
////                    callback.onError(e);
////                } finally {
////                    try {
////                        if (is != null) {
////                            is.close();
////                        }
////                        if (os != null) {
////                            os.close();
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        });
//
//
//        MyProgressListener myProgressListener;
//        MyRetrofitManager retrofitManager = new MyRetrofitManager(myProgressListener = new MyProgressListener() {
//            @Override
//            public void onStart() {
//                L.i("testz", "开始下载");
//                callback.onStart();
//            }
//
//            @Override
//            public void onProgress(float progress, long total) {
//                L.i("test", "-----------------APP下载进度: " + progress / total);
//                callback.onProgress(progress, total);
//
//            }
//
//            @Override
//            public void onFinish() {
//                callback.onSuccess(FileUtil.getFileByPath(path + "/" + fileName));//安装
//                L.i("testz", "下载完成,安装启动。" + path + "/" + fileName);
//            }
//
//            @Override
//            public void onFail(Throwable e) {
//                L.i("testz", "下载失败");
//                callback.onError(e);
//            }
//        });
//        retrofitManager.downApkFile(url, Constants.DOWNLOAD_APK_PATH, "test.apk", new Subscriber() {
//            @Override
//            public void onCompleted() {
//                myProgressListener.onFinish();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                myProgressListener.onFail(e);
//            }
//
//            @Override
//            public void onNext(Object object) {
//                myProgressListener.onStart();
//            }
//        });
//
//    }
//
//    @Override
//    public void cancelDownload(@NonNull String url) {
//        L.i("testz", "取消更新");
//    }
//
//
//    private Map<String, String> transform(Map<String, Object> params) {
//        Map<String, String> map = new TreeMap<>();
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            map.put(entry.getKey(), entry.getValue().toString());
//        }
//        return map;
//    }
//}
