package com.danqiu.online.edu.retrofit;

/**
 * 文件下载进度事件
 */
public interface XDownloadProgress {
    void onStartDownload();

    void onProgress(int progress, long total);

    void onFinishDownload();

    void onFail(Throwable throwable);
}
