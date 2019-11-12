package com.danqiu.online.edu.mvp.model;

import com.danqiu.online.edu.base.BaseMvpModel;
import com.danqiu.online.edu.mvp.contract.TestContract;
import com.danqiu.online.edu.mvp.listener.ResponseCallback;
import com.danqiu.online.edu.retrofit.ProgressSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzj on 2019/10/22
 * Describe ：参考Model
 */
public class TestModel extends BaseMvpModel implements TestContract.Model {
    public TestModel() {
        // 在这里做一些初始化操作
    }
    /**
     * 登录
     * @param account
     * @param password
     */
    @Override
    public void login(String account, String password, ResponseCallback callback) {
        Map<String,String> map=new HashMap<>();
        httpRxCallback.postLogin(map).compose(schedulersTransformer())
                .subscribe(new ProgressSubscriber() {
                    @Override
                    protected void _onNext(Object object) {
                        callback._onSucceed(object);
                    }

                    @Override
                    protected void _onError(String message) {
                        callback._onFail(message);
                    }

                    @Override
                    protected void _onCompleted() {
                        callback._onCompleted();
                    }
                });
    }
}
