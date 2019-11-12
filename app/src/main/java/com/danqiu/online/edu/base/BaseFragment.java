package com.danqiu.online.edu.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danqiu.online.edu.widget.XProgressDialog;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzj on 2019/10/21
 * Describe ：注释
 */
public abstract class BaseFragment <P extends BaseMvpPresenter> extends Fragment implements IBaseView {
    protected P mPresenter;
    public Activity fragmentActivity;
    public XProgressDialog xProgressDialog;
    public View mRootView;
    public EventBus eventBus;
    private Unbinder mUnbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
        xProgressDialog=new XProgressDialog(fragmentActivity);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(_setContentView(), container, false);
         eventBus = EventBus.getDefault();
        mPresenter=_createPresenter();
        if(mPresenter!=null){
            mPresenter.attachView(this);
        }

        _initBase();
        _onCreateView(savedInstanceState);
        return mRootView;
    }
    /**
     * 初始化
     */
    private void _initBase() {
        mUnbinder = ButterKnife.bind(this, mRootView);
        addToken();
    }
    /**
     * 初始化mPresenter
     */
    protected abstract P _createPresenter();

    /**
     * 初始化
     */
    protected abstract void _onCreateView(Bundle savedInstanceState);

    /**
     * 获取布局id
     * @return
     */
    protected abstract int _setContentView();
    public void registEvent() {
        if (!eventBus.isRegistered(this)) eventBus.register(this);
    }

    /**
     * 添加token
     */
    public void addToken(){
      //  mToken= PreferencesUtil.getInstance(fragmentActivity).getString("token","");
    }
    /**
     * 页面跳转
     * @param toActivity
     */
    public void intenToActivity(Class toActivity){
        Intent intent = new Intent(fragmentActivity,toActivity);
        startActivity(intent);
    }
    public void intenToActivityWithBundle(Class toActivity, Bundle bundle) {
        Intent intent = new Intent(fragmentActivity, toActivity);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }

    public void intenToActivityResult(Class toActivity, int requstCode) {
        Intent intent = new Intent(fragmentActivity, toActivity);
        startActivityForResult(intent, requstCode);
    }

    public void intenToActivityResultWithBundle(Class toActivity, int requstCode, Bundle bundle) {
        Intent intent = new Intent(fragmentActivity, toActivity);
        intent.putExtras(bundle);
        startActivityForResult(intent, requstCode);
    }

    public void intentFinishActivityResult(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        fragmentActivity.setResult(fragmentActivity.RESULT_OK, intent);
        fragmentActivity.finish();
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
    public void onDestroyView() {
        if (eventBus.isRegistered(this)) eventBus.unregister(this);
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
       mUnbinder.unbind();
    }

}
