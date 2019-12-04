package com.danqiu.online.edu.config;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.danqiu.online.edu.base.BaseActivity;
import com.danqiu.online.edu.fresco.ImageLoaderConfig;
import com.danqiu.online.edu.utils.ToastUtil;
import com.danqiu.online.edu.widget.MyRefreshFooter;
import com.danqiu.online.edu.widget.MyRefreshHeader;
import com.danqiu.online.edu.xupdate.MyUpdateHttpService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;

import androidx.annotation.NonNull;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

/**
 * Created by lzj on 2019/10/22
 * Describe ：注释
 */
public class MyApplication extends Application {
    //private HttpProxyCacheServer proxy;
    public static MyApplication app;
    public BaseActivity activity;
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout refreshLayout) {
                refreshLayout.setEnableLoadMoreWhenContentNotFull(false);//不足一页不刷新
                return new MyRefreshHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout refreshLayout) {
                return new MyRefreshFooter(context);
            }
        });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //配置缓存的初始化
        Fresco.initialize(this, ImageLoaderConfig.getImagePipelineConfig(this));

        initXUpdate();
    }

    private void initXUpdate() {
        XUpdate.get()
                .debug(true)
                .isWifiOnly(true)                                               //默认设置只在wifi下检查版本更新
                .isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                //.param("versionCode", UpdateUtils.getVersionCode(this))         //设置默认公共请求参数
                //.param("appKey", getPackageName())
                .param("paramKey","APPVersion,APPDownAddress")
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        if (error.getCode() != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
                            ToastUtil.showShort(getApplicationContext(),error.toString());
                            Log.i("test","---------更新错误"+error.getCode());
                        }
                    }
                })
                .setApkCacheDir( Constants.DOWNLOAD_APK_PATH )
                .supportSilentInstall(false)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new MyUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(this);                                                    //这个必须初始化

    }


}
