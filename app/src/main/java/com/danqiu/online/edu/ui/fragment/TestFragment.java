package com.danqiu.online.edu.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import com.danqiu.online.edu.base.BaseFragment;
import com.danqiu.online.edu.mvp.contract.TestContract;
import com.danqiu.online.edu.mvp.presenter.TestPresenter;

/**
 * Created by lzj on 2019/10/22
 * Describe ：注释
 */
public class TestFragment extends BaseFragment <TestPresenter>implements TestContract.View{
    @Override
    protected TestPresenter _createPresenter() {
        return new TestPresenter();
    }

    @Override
    protected void _onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int _setContentView() {
        return 0;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(Object object) {

    }
}
