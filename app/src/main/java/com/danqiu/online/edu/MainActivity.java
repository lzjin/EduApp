package com.danqiu.online.edu;

import android.content.Context;
import android.os.Bundle;

import com.danqiu.online.edu.base.BaseActivity;
import com.danqiu.online.edu.base.IBasePresenter;
import com.danqiu.online.edu.ui.activity.TestActivity;
import com.danqiu.online.edu.utils.IntentUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected IBasePresenter _createPresenter() {
        return null;
    }

    @Override
    protected void _onCreate(Bundle savedInstanceState) {
        IntentUtil.IntenToActivity(this, TestActivity.class);
    }

    @Override
    protected int _setContentView() {
        return R.layout.activity_main;
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
}
