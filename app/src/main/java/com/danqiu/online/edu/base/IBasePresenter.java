package com.danqiu.online.edu.base;

/**
 * Created by lzj on 2019/10/21
 * Describe ：注释
 */
public interface IBasePresenter <V extends IBaseView> {
    /**
     * 绑定View
     * @param view
     */
    void attachView(V view);

    /**
     * 解绑view
     */
    void detachView();

    /**
     * 得到view
     */
    V getView();
}
