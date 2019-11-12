package com.danqiu.online.edu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.danqiu.online.edu.R;
import com.danqiu.online.edu.fresco.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by lzj on 2019/8/5
 * Describe ：注释
 */
public class MyRefreshHeader extends LinearLayout implements RefreshHeader {

    public static String REFRESH_FOOTER_LOADING = "下拉刷新";
    public static String REFRESH_FOOTER_FINISH = "刷新中...";
    public static String REFRESH_FOOTER_FAILED = "加载失败";
    public static String REFRESH_FOOTER_ALLLOADED = "没有更多数据了";

    private View      mViewHeader;
    private TextView  mTitleText;
    private SimpleDraweeView mImageView;

    public MyRefreshHeader(Context context) {
        super(context);
        this.initView(context);
    }

    public MyRefreshHeader(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public MyRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    /**
     * 初始化界面布局
     * @param context
     */
    private void initView(Context context) {
        mViewHeader = LayoutInflater.from(context).inflate(R.layout.view_loading_listview_header, null, false);
        mTitleText=mViewHeader.findViewById(R.id.tv_content);
        mImageView=mViewHeader.findViewById(R.id.iv_image);

        ImageLoader.loadImageGif(mImageView, R.mipmap.iv_refresh);
        mTitleText.setText(REFRESH_FOOTER_LOADING);
        addView(mViewHeader);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    /**
     *  获取变换方式（必须指定一个：平移、拉伸、固定、全屏）,Translate指平移，大多数都是平移
     */
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
       return SpinnerStyle.Translate;//指定为平移，不能null
    }
    /**
     * 6，结束下拉刷新的时候需要关闭动画
     *
     * @param refreshLayout
     * @param success
     * @return
     */
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        //        if (success){
//            mTitleText.setText(REFRESH_FOOTER_FINISH);
//        } else {
//            mTitleText.setText(REFRESH_FOOTER_FAILED);
//        }
//        return 500;//延迟500毫秒之后再弹回底部
        return 0;
    }
    /**
     * 设置主题颜色
     * @param colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
     */
    @Override
    public void setPrimaryColors(int... colors) {

    }


    /**
     * 尺寸定义完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     * @param kernel RefreshKernel
     * @param height HeaderHeight or FooterHeight
     * @param extendHeight extendHeaderHeight or extendFooterHeight
     */
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        if (percent < 1) {
            mImageView.setScaleX(percent);
            mImageView.setScaleY(percent);
        }
    }

//    /**
//     * 手指拖动下拉（会连续多次调用）
//     * @param percent 下拉的百分比 值 = offset/footerHeight (0 - percent - (footerHeight+extendHeight) / footerHeight )
//     * @param offset 下拉的像素偏移量  0 - offset - (footerHeight+extendHeight)
//     * @param height 高度 HeaderHeight or FooterHeight
//     * @param extendHeight 扩展高度  extendHeaderHeight or extendFooterHeight
//     */
//    @Override
//    public void onPulling(float percent, int offset, int height, int extendHeight) {
//        if (percent < 1) {
//            mImageView.setScaleX(percent);
//            mImageView.setScaleY(percent);
//        }
//    }
//    /**
//     * 手指释放之后的持续动画（会连续多次调用）
//     * @param percent 下拉的百分比 值 = offset/footerHeight (0 - percent - (footerHeight+extendHeight) / footerHeight )
//     * @param offset 下拉的像素偏移量  0 - offset - (footerHeight+extendHeight)
//     * @param height 高度 HeaderHeight or FooterHeight
//     * @param extendHeight 扩展高度  extendHeaderHeight or extendFooterHeight
//     */
//    @Override
//    public void onReleasing(float percent, int offset, int height, int extendHeight) {
//
//    }
    /**
     * 释放时刻（调用一次，将会触发加载）
     * @param refreshLayout RefreshLayout
     * @param height 高度 HeaderHeight or FooterHeight
     * @param extendHeight 扩展高度  extendHeaderHeight or extendFooterHeight
     */
    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    /**
     * 水平方向的拖动
     * @param percentX 下拉时，手指水平坐标对屏幕的占比（0 - percentX - 1）
     * @param offsetX 下拉时，手指水平坐标对屏幕的偏移（0 - offsetX - LayoutWidth）
     * @param offsetMax 最大的偏移量
     */
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }
    /**
     * 是否支持水平方向的拖动（将会影响到onHorizontalDrag的调用）
     * @return 水平拖动需要消耗更多的时间和资源，所以如果不支持请返回false
     */
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
    /**
     *  一般可以理解为一下case中的三种状态，在达到相应状态时候开始改变
     * 注意：这三种状态都是初始化的状态
     */
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            //1,下拉刷新的开始状态：下拉可以刷新
            case PullDownToRefresh:
               // mImage.setImageResource(R.drawable.commonui_pull_image);
                mTitleText.setText(REFRESH_FOOTER_LOADING);
                break;
            //2,下拉到最底部的状态：释放立即刷新
            case ReleaseToRefresh:
               // mImage.setImageResource(R.drawable.anim_pull_end);
                mTitleText.setText(REFRESH_FOOTER_LOADING);
                //mAnimPull = (AnimationDrawable) mImage.getDrawable();
               // mAnimPull.start();
                break;
            //3,下拉到最底部后松手的状态：正在刷新
            case Refreshing:
               // mImage.setImageResource(R.drawable.anim_pull_refreshing);
                mTitleText.setText(REFRESH_FOOTER_FINISH);
              //  mAnimRefresh = (AnimationDrawable) mImage.getDrawable();
               // mAnimRefresh.start();
                break;
        }
    }
}
