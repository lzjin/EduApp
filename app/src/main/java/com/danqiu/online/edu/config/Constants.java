package com.danqiu.online.edu.config;

import android.os.Environment;

/**
 * 常量
 */
public class Constants {
    public static final String APP_ID = "wx96e4c38cb86d9b98";//wx88888888
    public static final String APP_SECRET = "f11d8da719f07bf539d0410230918f40";//wx88888888
    public static final String SAVE_DATA_NAME = "SavaData";
    public static final String APP_ROOT_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/AA_danqiu/";//根路径
    public static final String IMA_PATH= APP_ROOT_PATH+"/user_images/";//图片
    public static final String DOWNLOAD_APK_PATH=APP_ROOT_PATH+"/download";//下载路径
    public static final String APK_FILE_NAME="xxx.apk";//apk文件名
    public static final int SEND_CODE_TIME=30;
    public static final double MONEY_PROPORTION=100.0;//金额换算为元(分)
    public static final int INTERVAL_SEND=1000*60*5;//5分钟发送一次

    public static final long  COUNT_DOWN_TIME_15_M=15* 60*1000;// 倒计时间隔15分钟

}
