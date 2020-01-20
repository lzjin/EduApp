package com.danqiu.online.edu.ui.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps.MapView;
import com.danqiu.online.edu.R;
import com.danqiu.online.edu.base.BaseActivity;
import com.danqiu.online.edu.dialog.BaseDialog;
import com.danqiu.online.edu.dialog.InputDialog;
import com.danqiu.online.edu.dialog.MessageDialog;
import com.danqiu.online.edu.dialog.ToastDialog;
import com.danqiu.online.edu.dialog.WaitDialog;
import com.danqiu.online.edu.mvp.contract.TestContract;
import com.danqiu.online.edu.mvp.presenter.TestPresenter;
import com.danqiu.online.edu.utils.L;
import com.danqiu.online.edu.utils.ToastUtil;
import com.danqiu.online.edu.xupdate.MyUpdateParser;
import com.hjq.toast.ToastUtils;
import com.xuexiang.xupdate.XUpdate;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by lzj on 2019/10/22
 * Describe ：注释
 */
public class TestActivity extends BaseActivity<TestPresenter> implements TestContract.View, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.edt_user)
    EditText edtUser;
    @BindView(R.id.edt_pass)
    EditText edtPass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    String mUpdateUrl = "http://www.danqiuedu.com/uapi/api/pub/sysConfig/getVal";
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.btn_dialog_succeed_toast)
    Button btnDialogSucceedToast;
    @BindView(R.id.btn_dialog_fail_toast)
    Button btnDialogFailToast;
    @BindView(R.id.btn_dialog_warn_toast)
    Button btnDialogWarnToast;
    @BindView(R.id.btn_dialog_wait)
    Button btnDialogWait;
    @BindView(R.id.btn_dialog_message)
    Button btnDialogMessage;
    @BindView(R.id.btn_dialog_input)
    Button btnDialogInput;

    @Override
    protected TestPresenter _createPresenter() {
        return new TestPresenter();
    }

    @Override
    protected void _onCreate(Bundle savedInstanceState) {
        requestPermission();


        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected int _setContentView() {
        return R.layout.activity_test;
    }


    @OnClick({R.id.btn_login,R.id.btn_dialog_succeed_toast, R.id.btn_dialog_fail_toast,
            R.id.btn_dialog_warn_toast, R.id.btn_dialog_wait, R.id.btn_dialog_message, R.id.btn_dialog_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_succeed_toast:
                // 成功对话框
                new ToastDialog.Builder(this)
                        .setType(ToastDialog.Type.FINISH)
                        .setMessage("完成")
                        .show();
                break;
            case R.id.btn_dialog_fail_toast:
                // 失败对话框
                new ToastDialog.Builder(this)
                        .setType(ToastDialog.Type.ERROR)
                        .setMessage("错误")
                        .show();
                break;
            case R.id.btn_dialog_warn_toast:
                // 警告对话框
                new ToastDialog.Builder(this)
                        .setType(ToastDialog.Type.WARN)
                        .setMessage("警告")
                        .show();
                break;
            case R.id.btn_dialog_wait:
                // 等待对话框
                final BaseDialog dialog = new WaitDialog.Builder(this)
                        // 消息文本可以不用填写
                        .setMessage(getString(R.string.common_loading))
                        .show();
                dialog.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);
                break;
            case R.id.btn_dialog_message:
                new MessageDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle("我是标题")
                        // 内容必须要填写
                        .setMessage("我是内容")
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                ToastUtils.show("确定了");
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                ToastUtils.show("取消");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_input:
                // 输入对话框
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle("我是标题")
                        // 内容可以不用填写
                        .setContent("我是内容")
                        // 提示可以不用填写
                        .setHint("我是提示")
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                ToastUtils.show("确定了"+content);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                ToastUtils.show("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_login:
                onLogin();
                break;
        }
    }

    public void onLogin() {
        // 登录操作
        // mPresenter.login(edtUser.getText().toString().trim(), edtPass.getText().toString().trim());

        getAppVersion2();
        getAppVersion();
    }

    /**
     * app更新
     */
    private void getAppVersion2() {

        XUpdate.newBuild(activity)
                .isGet(true)
                .updateUrl(mUpdateUrl)
                .updateParser(new MyUpdateParser()) //设置自定义的版本更新解析器
                .supportBackgroundUpdate(true)
                .update();
    }

    /**
     * app更新
     */
    private void getAppVersion() {

//        Map<String, String> map = new HashMap<>();
//        map.put("paramKey", "APPVersion,APPDownAddress");
//
//        new UpdateAppManager
//                .Builder()
//                .setActivity(this)
//                .setPost(false)
//                // .setAppKey()
//                .setParams(map)
//                //更新地址
//                .setUpdateUrl(ApiService.updateApp)
//                //全局异常捕获
//                .handleException(new ExceptionHandler() {
//                    @Override
//                    public void onException(Exception e) {
//                        e.printStackTrace();
//                    }
//                })
//                //实现httpManager接口的对象
//                .setHttpManager(new MyUpdateAppHttpUtil())
//                .build()
//                .checkNewApp(new MyUpdateCallback());
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
        L.i("test", "test---------------回调");
        dismissDialog();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onError(String error) {
        ToastUtil.showShort(this, error);
    }

    @Override
    public void onSuccess(Object object) {
        ToastUtil.showShort(this, "登录成功");
    }

    /**
     * 允许权限成功后触发
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        ToastUtil.showShort(this, "授权成功");
    }

    /**
     * 禁止权限后触发
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtil.showShort(this, "您已拒绝相关权限，可到设置里自行开启");
    }

    /**
     * 请求授权
     */
    @AfterPermissionGranted(1001)
    private void requestPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //授权成功后
            ToastUtil.showShort(this, "授权成功");
        } else {
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
