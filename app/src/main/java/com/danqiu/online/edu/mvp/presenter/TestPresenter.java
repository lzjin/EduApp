package com.danqiu.online.edu.mvp.presenter;

import com.danqiu.online.edu.base.BaseMvpPresenter;
import com.danqiu.online.edu.mvp.contract.TestContract;
import com.danqiu.online.edu.mvp.listener.ResponseCallback;
import com.danqiu.online.edu.mvp.model.TestModel;

/**
 * Created by lzj on 2019/10/21
 * Describe ：参考Presenter类
 */
public class TestPresenter extends BaseMvpPresenter<TestContract.View> implements TestContract.Presenter {

    public TestModel mModel;

    public TestPresenter() {
        this.mModel = new TestModel();
    }

    /**
     * 登录
     * @param account
     * @param password
     */
    @Override
    public void login(String account, String password) {
        getView().onLoading();
        mModel.login(account,password, new ResponseCallback() {
            @Override
            public void _onSucceed(Object object) {
                if(getView()!=null){
                    getView().onSuccess(object);
                }
            }
            @Override
            public void _onFail(String msg) {
                if(getView()!=null){
                    getView().onError(msg);
                }
            }
            @Override
            public void _onCompleted() {
                if(getView()!=null){
                    getView().onComplete();
                }
            }
        });
    }

}
