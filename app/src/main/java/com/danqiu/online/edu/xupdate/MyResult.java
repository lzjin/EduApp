package com.danqiu.online.edu.xupdate;

import java.io.Serializable;

/**
 * Created by lzj on 2019/11/11
 * Describe ：注释
 */
public class MyResult  implements Serializable {


    /**
     * data : {"hasUpdate":true,"ignorable":true,"isForce":true,"IsSilent":true,"IsAutoInstall":true,"versionCode":1,"versionName":"1.0","updateContent":"本次更新内容","apkUrl":"http: //www.xxx.com","apkSize":20}
     * code : 10000
     * message : 操作成功
     */

    private DataBean data;
    private String code;
    private String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * hasUpdate : true
         * ignorable : true
         * isForce : true
         * IsSilent : true
         * IsAutoInstall : true
         * versionCode : 1
         * versionName : 1.0
         * updateContent : 本次更新内容
         * apkUrl : http: //www.xxx.com
         * apkSize : 20
         */

        private boolean hasUpdate;
        private boolean ignorable;
        private boolean isForce;
        private boolean IsSilent;
        private boolean IsAutoInstall;
        private int versionCode;
        private String versionName;
        private String updateContent;
        private String apkUrl;
        private long apkSize;
        private String APPDownAddress;

        public String getAPPDownAddress() {
            return APPDownAddress;
        }

        public void setAPPDownAddress(String APPDownAddress) {
            this.APPDownAddress = APPDownAddress;
        }

        public boolean isHasUpdate() {
            return hasUpdate;
        }

        public void setHasUpdate(boolean hasUpdate) {
            this.hasUpdate = hasUpdate;
        }

        public boolean isIgnorable() {
            return ignorable;
        }

        public void setIgnorable(boolean ignorable) {
            this.ignorable = ignorable;
        }

        public boolean isIsForce() {
            return isForce;
        }

        public void setIsForce(boolean isForce) {
            this.isForce = isForce;
        }

        public boolean isIsSilent() {
            return IsSilent;
        }

        public void setIsSilent(boolean IsSilent) {
            this.IsSilent = IsSilent;
        }

        public boolean isIsAutoInstall() {
            return IsAutoInstall;
        }

        public void setIsAutoInstall(boolean IsAutoInstall) {
            this.IsAutoInstall = IsAutoInstall;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        public String getApkUrl() {
            return apkUrl;
        }

        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }

        public long getApkSize() {
            return apkSize;
        }

        public void setApkSize(long apkSize) {
            this.apkSize = apkSize;
        }
    }
}
