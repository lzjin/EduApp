package com.danqiu.online.edu.xupdate;

/**
 * Created by lzj on 2019/11/12
 * Describe ：下载进度监听
 */
public interface MyProgressListener {

    void onStart();

    void onProgress(float progress, long total);

    void onFinish();

    void onFail(Throwable throwable);

}
