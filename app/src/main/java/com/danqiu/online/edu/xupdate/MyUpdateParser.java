package com.danqiu.online.edu.xupdate;

import android.util.Log;

import com.google.gson.Gson;
import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;

/**
 * Created by lzj on 2019/11/11
 * Describe ：自定义返回json
 */
public class MyUpdateParser implements IUpdateParser {
    @Override
    public UpdateEntity parseJson(String jsonData) throws Exception {
        Log.i("test","---------解析版本json="+jsonData);
        Gson gson = new Gson();
        MyResult result = gson.fromJson(jsonData, MyResult.class);
        if (result != null) {

//            UpdateEntity  updateEntity=new UpdateEntity();
//            updateEntity.setHasUpdate(result.getData().isHasUpdate())//是否有新版本
//                    .setIsSilent(result.getData().isIsSilent())//是否静默下载
//                    .setIsAutoInstall(result.getData().isIsAutoInstall())//是否下载完成后自动安装
//                    .setForce(result.getData().isHasUpdate())  //是否强制安装
//                    .setIsIgnorable(result.getData().isIgnorable()) //是否可忽略该版本
//                    .setVersionCode(result.getData().getVersionCode())
//                    .setVersionName(result.getData().getVersionName())
//                    .setUpdateContent(result.getData().getUpdateContent())
//                    .setDownloadUrl(result.getData().getApkUrl())
//                    .setSize(result.getData().getApkSize());


            DownloadEntity entity=new DownloadEntity();
            entity.setShowNotification(true);
            entity.setDownloadUrl(result.getData().getAPPDownAddress());
            entity.setMd5("");
            UpdateEntity  updateEntity=new UpdateEntity();
            updateEntity.setHasUpdate(true)//是否有新版本
                    .setForce(false)  //是否强制安装
                    .setIsAutoInstall(true)//是否下载后安装
                    .setIsSilent(false)//是否静默下载
                    .setIsIgnorable(false) //是否可忽略该版本
                    .setVersionCode(2)
                    .setVersionName("2.0")
                    .setUpdateContent("1、优化弹框\n" + " 2、是多少大多\n" + " 3、地方是的辅")
                    .setDownloadUrl(result.getData().getAPPDownAddress())
                    //.setDownLoadEntity(entity)
                    .setSize(13560);

            return updateEntity;

        }

        return null;
    }

}
