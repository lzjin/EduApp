package com.danqiu.online.edu.base;

import android.content.Context;

/**
 * Created by lzj on 2019/10/21
 * Describe ：注释
 */
public interface IBaseView  {

    /**
     * 获取上下文对象
     */
    Context getContext();

    /**
     * 加载中
     */
    void onLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 加载完成
     */
    void onComplete();

    /**
     * 用于请求的数据为空的状态
     */
    void onEmpty();

    /**
     * 用于请求数据出错
     */
    void onError(String error);
}
