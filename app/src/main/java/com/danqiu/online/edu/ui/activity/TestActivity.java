package com.danqiu.online.edu.ui.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.danqiu.online.edu.R;
import com.danqiu.online.edu.base.BaseActivity;
import com.danqiu.online.edu.mvp.contract.TestContract;
import com.danqiu.online.edu.mvp.presenter.TestPresenter;
import com.danqiu.online.edu.retrofit.ApiService;
import com.danqiu.online.edu.utils.L;
import com.danqiu.online.edu.utils.ToastUtil;
import com.danqiu.online.edu.xupdate.MyUpdateAppHttpUtil;
import com.danqiu.online.edu.xupdate.MyUpdateCallback;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.listener.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by lzj on 2019/10/22
 * Describe ：注释
 */
public class TestActivity extends BaseActivity<TestContract.Presenter> implements TestContract.View,EasyPermissions.PermissionCallbacks {

    @BindView(R.id.edt_user)
    EditText edtUser;
    @BindView(R.id.edt_pass)
    EditText edtPass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    String mUpdateUrl="http://www.danqiuedu.com/uapi/api/pub/sysConfig/getVal";
    @Override
    protected TestContract.Presenter _createPresenter() {
        return new TestPresenter();
    }

    @Override
    protected void _onCreate(Bundle savedInstanceState) {
        requestPermission();
    }

    @Override
    protected int _setContentView() {
        return R.layout.activity_test;
    }


    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        onLogin();
    }

    public void onLogin() {
        // 登录操作
       // mPresenter.login(edtUser.getText().toString().trim(), edtPass.getText().toString().trim());

//        XUpdate.newBuild(activity)
//                .isGet(true)
//                .updateUrl(mUpdateUrl)
//                .updateParser(new MyUpdateParser()) //设置自定义的版本更新解析器
//                .supportBackgroundUpdate(true)
//                .update();

        getAppVersion();
    }

    /**
     * app更新
     */
    private void getAppVersion(){

        Map<String,String> map=new HashMap<>();
        map.put("paramKey","APPVersion,APPDownAddress");

        new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setPost(false)
                // .setAppKey()
                .setParams(map)
                //更新地址
                .setUpdateUrl(ApiService.updateApp)
                //全局异常捕获
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                //实现httpManager接口的对象
                .setHttpManager(new MyUpdateAppHttpUtil())
                .build()
                .checkNewApp(new MyUpdateCallback());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLoading() {
       showDialog("登录中...");
    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onComplete() {
        L.i("test","test---------------回调");
        dismissDialog();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onError(String error) {
        ToastUtil.showShort(this,error);
    }

    @Override
    public void onSuccess(Object object) {
        ToastUtil.showShort(this,"登录成功");
    }
    /**
     * 允许权限成功后触发
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        ToastUtil.showShort(this,"授权成功");
    }
    /**
     * 禁止权限后触发
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtil.showShort(this,"您已拒绝相关权限，可到设置里自行开启");
    }
    /**
     * 请求授权
     */
    @AfterPermissionGranted(1001)
    private void requestPermission(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //授权成功后
            ToastUtil.showShort(this,"授权成功");
        }else {
            //没有权限的话，先提示，点确定后弹出系统的授权提示框
            EasyPermissions.requestPermissions(this, "为了更好的用户体验需要获取以下权限", 1001, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}
