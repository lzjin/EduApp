package com.danqiu.online.edu.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.danqiu.online.edu.widget.XProgressDialog;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzj on 2019/10/21
 * Describe ：注释
 */
public abstract  class BaseActivity <P extends IBasePresenter> extends AppCompatActivity implements IBaseView {
    protected P mPresenter;
    public BaseActivity activity;
    public XProgressDialog xProgressDialog;
    public EventBus eventBus;
    private Unbinder mUnbinder;//释放资源用
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        xProgressDialog=new XProgressDialog(this);
        mPresenter=_createPresenter();
        if(mPresenter != null){
            mPresenter.attachView(this);
        }

        setContentView(_setContentView());
        _initBase();
        _onCreate(savedInstanceState);
    }
    /**
     * 初始化
     */
    private void _initBase() {
          initSoftKeyboard();
          mUnbinder = ButterKnife.bind(this);//绑定注解
//        mManagerActivity = ActivityController.getInstance(); //把activity放入栈里面进行管理
//        mManagerActivity.pushOneActivity(this);
        setStatusBar();//状态栏
        addToken();
        eventBus = EventBus.getDefault();
    }

    /**
     * 添加token
     */
    public void addToken(){
       // mToken= PreferencesUtil.getInstance(activity).getString("token","");
    }

    /**
     *   基类统一设置
     *   白底黑字
     */
    protected void setStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).fitsSystemWindows(true).init();
    }
    /**
     * 初始化mPresenter
     */
    protected abstract P _createPresenter();

    /**
     * 初始化
     */
    protected abstract void _onCreate(Bundle savedInstanceState);

    /**
     * 获取布局id
     * @return
     */
    protected abstract int _setContentView();
    /**
     * 注册eventbus
     */
    public void registEvent() {
        if (!eventBus.isRegistered(this)) eventBus.register(this);
    }
    /**
     * 页面跳转
     * @param toActivity
     */
    public void intenToActivity(Class toActivity){
        Intent intent = new Intent(activity,toActivity);
        startActivity(intent);
    }
    public void intenToActivityWithBundle(Class toActivity, Bundle bundle) {
        Intent intent = new Intent(activity, toActivity);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }

    public void intenToActivityResult(Class toActivity, int requstCode) {
        Intent intent = new Intent(activity, toActivity);
        startActivityForResult(intent, requstCode);
    }

    public void intenToActivityResultWithBundle(Class toActivity, int requstCode, Bundle bundle) {
        Intent intent = new Intent(activity, toActivity);
        intent.putExtras(bundle);
        startActivityForResult(intent, requstCode);
    }

    public void intentFinishActivityResult(Bundle bundle,int resultCode) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(resultCode,intent);
        finish();
    }
    /**
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }
    /**
     * 初始化软键盘
     */
    protected void initSoftKeyboard() {
        // 点击外部隐藏软键盘，提升用户体验
        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     *  加载进度 1
     */
    public void showDialog() {
        xProgressDialog.setTitle("加载中");
        xProgressDialog.show();
    }

    public void showDialog(String title) {
        xProgressDialog.setTitle(title);
        xProgressDialog.onShow();
    }
    public void dismissDialog() {
        xProgressDialog.onDismiss();
    }

    @Override
    protected void onDestroy() {
        if (eventBus.isRegistered(this)) eventBus.unregister(this);
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
//        mManagerActivity.popOneActivity(this);
          mUnbinder.unbind();
    }
}
