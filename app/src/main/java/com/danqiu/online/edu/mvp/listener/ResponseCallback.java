package com.danqiu.online.edu.mvp.listener;

/**
 * Created by lzj on 2019/10/22
 * Describe ：注释
 */
public interface ResponseCallback {
    void _onSucceed(Object object);

    void _onFail(String msg);

    void _onCompleted();
}
