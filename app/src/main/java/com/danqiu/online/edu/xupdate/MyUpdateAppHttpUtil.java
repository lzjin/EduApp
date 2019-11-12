package com.danqiu.online.edu.xupdate;

import com.danqiu.online.edu.retrofit.ApiService;
import com.danqiu.online.edu.retrofit.RetrofitManager;
import com.danqiu.online.edu.utils.FileUtil;
import com.danqiu.online.edu.utils.L;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lzj on 2019/11/12
 * Describe ：注释
 */
public class MyUpdateAppHttpUtil implements HttpManager {

    private ApiService httpRxCallback = RetrofitManager.getInstance().apiService;

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {
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
                        callBack.onError(e.getMessage().toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            callBack.onResponse(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        //异步请求
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage().toString());
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
                    long total = response.body().contentLength();
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        L.i("test", "-----------------APP下载进度: " + progress);
                        callback.onProgress(sum, total);  //下载中更新进度条
                    }
                    os.flush();
                    L.i("testz", "下载完成,安装启动。" + path + "/" + fileName);
                    callback.onResponse(file);   //下载完成
                } catch (Exception e) {
                    callback.onError(e.getMessage().toString());
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
    }
}
