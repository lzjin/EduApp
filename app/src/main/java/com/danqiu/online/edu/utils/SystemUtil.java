package com.danqiu.online.edu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.danqiu.online.edu.config.Constants;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.FileProvider;

/**
 * Created by lzj on 2019/7/1
 * Describe ：注释
 */
public class SystemUtil {

    /**
     * 安装APK文件
     * 兼容7.0版本
     */
    public static void installApk(Context context, String apkPath, String apkName) {
        File apkfile = new File(apkPath, apkName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //包名+fileprovider
            Uri fileUri = FileProvider.getUriForFile(context, SystemUtil.getPackageName(context) + ".fileprovider", apkfile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//临时uri授权
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            L.i("test", "------------安装位置:" + fileUri);
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 版本code号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean matcherPhone(String phone) {
        Pattern pattern = Pattern.compile("[1][3456789]\\d{9}");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static int getScreenHeightPx(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (windowManager != null) {
//            windowManager.getDefaultDisplay().getMetrics(dm);
            windowManager.getDefaultDisplay().getRealMetrics(dm);
            return dm.heightPixels;
        }
        return 0;

    }

    /**
     * statusBar高度
     **/
    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 可见屏幕高度
     **/
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    /**
     * 隐藏Android底部的虚拟按键
     */
    public static void hideVirtualKey(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setAttributes(params);
    }

    /**
     * 获取当前系统时间 精确到秒
     *
     * @return
     */
    public static String currentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newData = sdf.format(d);
        L.i("test", "--------------现在日期：" + newData);
        return newData;
    }

    /**
     * strlong--strDate  转化时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getTime(Date date) {
        if (date == null) {
            return "";
        }
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return strDate;
    }
    /**
     * 保留2位小数
     */
    public static String getPrice(double price) {
        if(price<=0){
            return "0";
        }
        double prices=price / Constants.MONEY_PROPORTION;
        String result=String.format("%.2f", prices);
        int index = result.indexOf(".");
        if (index != -1) {
            String end=result.substring(index, result.length());
            if(end.equals(".00")){
                result=result.substring(0, index);
            }
        }
        return result;
    }

    /**
     * 去位
     */
    public static String getPrice2(double price) {
        String numbers = price + "";
        if (numbers.equals("")) {
            return "";
        } else {
            int index = numbers.indexOf(".");
            if (index != -1) {
                return numbers.substring(0, index);
            } else {
                return numbers;
            }
        }

    }

    /**
     * 截断
     *
     * @param utcTime UTC时间
     * @return 2018-10-15T15:22:26.000+0800
     */
    public static String utcLocal(String utcTime) {
       if (utcTime.equals("")) {
            return "";
        }
       if(utcTime.contains("T")){
           return utcTime.substring(0, utcTime.indexOf("T"));
       }
       else {
           return "";
       }
    }


    /**
     * UTC时间 ---> 当地时间
     *
     * @param utcTime UTC时间
     * @return 推荐(根据手机系统时区)
     */
    public static String utcLocalTime(String utcTime) {
        if (utcTime.equals("")) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");//注意格式化的表达式
        try {
            Date time = format.parse(utcTime);
            utcTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcTime;
    }


    /**
     * 年级  ==8
     *
     * @param grade
     * @return
     */
    public static int getGrade(String grade) {
        int result = 0;
        switch (grade) {
            case "一年级":
                result = 1;
                break;
            case "二年级":
                result = 2;
                break;
            case "三年级":
                result = 3;
                break;
            case "四年级":
                result = 4;
                break;
            case "五年级":
                result = 5;
                break;
            case "六年级":
                result = 6;
                break;
            case "初一":
                result = 7;
                break;
            case "初二":
                result = 8;
                break;
            case "初三":
                result = 9;
                break;
            case "高一":
                result = 10;
                break;
            case "高二":
                result = 11;
                break;
            case "高三":
                result = 12;
                break;
        }
        return result;
    }

    /**
     * 年级  ==8
     *
     * @param grade
     * @return
     */
    public static String getGradeStr(int grade) {
        String result="";
        switch (grade) {
            case 1:
                result = "一年级";
                break;
            case 2:
                result = "二年级";
                break;
            case 3:
                result = "三年级";
                break;
            case 4:
                result = "四年级";
                break;
            case 5:
                result = "五年级";
                break;
            case 6:
                result = "六年级";
                break;
            case 7:
                result = "初一";
                break;
            case 8:
                result = "初二";
                break;
            case 9:
                result = "初三";
                break;
            case 10:
                result = "高一";
                break;
            case 11:
                result = "高二";
                break;
            case 12:
                result = "高三";
                break;
        }
        return result;
    }

    /**
     * 时间 算 年级
     *
     * @param date
     * @return
     */
    public static String getCurrentGrade(Date date) {

        String result = "毕业";
        Calendar now = Calendar.getInstance();
        Calendar ad = Calendar.getInstance();
        ad.setTime(date);
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        int adYear = ad.get(Calendar.YEAR);
        int grade = year - adYear;

        if (month < 9) {
            grade -= 1;
        }
        switch (grade) {
            case 0:
            case 1:
            case 2:
                result = "幼儿园";
                break;
            case 3:
                result = "一年级";
                break;
            case 4:
                result = "二年级";
                break;
            case 5:
                result = "三年级";
                break;
            case 6:
                result = "四年级";
                break;
            case 7:
                result = "五年级";
                break;
            case 8:
                result = "六年级";
                break;
            case 9:
                result = "初一";
                break;
            case 10:
                result = "初二";
                break;
            case 11:
                result = "初三";
                break;
            case 12:
                result = "高一";
                break;
            case 13:
                result = "高二";
                break;
            case 14:
                result = "高三";
                break;
        }
        L.i("test", "--------------------------年级=" + result);
        return result;
    }

}
