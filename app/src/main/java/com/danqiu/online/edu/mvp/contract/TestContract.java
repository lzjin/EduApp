package com.danqiu.online.edu.mvp.contract;

import com.danqiu.online.edu.base.IBaseModel;
import com.danqiu.online.edu.base.IBasePresenter;
import com.danqiu.online.edu.base.IBaseView;
import com.danqiu.online.edu.mvp.listener.ResponseCallback;

/**
 * Created by lzj on 2019/10/21
 * Describe ：参考契约类
 */
public interface TestContract {

    interface View extends IBaseView {
        void onSuccess(Object object);
    }

    interface Presenter extends IBasePresenter<View> {
        void login(String account, String password);
    }

    interface Model extends IBaseModel {
        void login(String account, String password, ResponseCallback callback);
    }

}
