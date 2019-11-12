package com.danqiu.online.edu.xupdate;

import com.google.gson.Gson;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import org.json.JSONObject;

/**
 * Created by lzj on 2019/11/12
 * Describe ：注释
 */
public class MyUpdateCallback extends UpdateCallback {
    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    protected UpdateAppBean parseJson(String json) {

        Gson gson = new Gson();
        MyResult result = gson.fromJson(json, MyResult.class);
        UpdateAppBean updateAppBean = new UpdateAppBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            updateAppBean.setUpdate("Yes")
                    //存放json，方便自定义解析
                    .setOriginRes(json)
                    .setNewVersion( "2.0")
                    .setApkFileUrl(result.getData().getAPPDownAddress())
                    .setTargetSize("13.56")
                    .setUpdateLog("1、地方的地方所发生的")
                    .setConstraint(true)
                    .setNewMd5("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateAppBean;
    }

    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
        updateAppManager.showDialogFragment();
    }

    /**
     * 网路请求之后
     */
    protected void onAfter() {

    }


    /**
     * 没有新版本
     * @param error HttpManager实现类请求出错返回的错误消息，交给使用者自己返回，有可能不同的应用错误内容需要提示给客户
     */
    protected void noNewApp(String error) {
    }

    /**
     * 网络请求之前
     */
    protected void onBefore() {
    }
}
