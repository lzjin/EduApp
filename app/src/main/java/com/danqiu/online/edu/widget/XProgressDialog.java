package com.danqiu.online.edu.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.danqiu.online.edu.R;


/**
 * Created by Administrator on 2018/12/20.
 * 加载进度对话框
 *
 * new XProgressDialog(this).onShow();
 * new XProgressDialog(this,"登录中",true).onShowN(true,false);
 */

public class XProgressDialog extends AlertDialog {
    private Context mContext;
    private View mView;
    private TextView mTitleTxt;
    private boolean isBack=true;
    private boolean isOutside=false;

    public XProgressDialog(Context context) {
        super(context, R.style.xProgressDialog);
        this.mContext=context;
        initView();
    }

    public XProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_xprogress_loading);
    }

    public void initView() {
        mView=LayoutInflater.from(mContext).inflate(R.layout.view_xprogress_loading, null);
        mTitleTxt = (TextView) mView.findViewById(R.id.tv_load_dialog);
        mTitleTxt.setText("加载中");
    }
    public XProgressDialog setTitle(String text){
        this.mTitleTxt.setText(text);
        return this;
    }
    public XProgressDialog onShow(boolean iBack,boolean iOutside){
        this.setCancelable(iBack);//点击返回是否关闭
        this.setCanceledOnTouchOutside(iOutside);//点击空白是否关闭
        this.show();
        return this;
    }
    public XProgressDialog onShow(){
        this.setCancelable(isBack);//点击返回是否关闭
        this.setCanceledOnTouchOutside(isOutside);//点击空白是否关闭
        this.show();
        return  this;
    }
    public XProgressDialog onDismiss(){
        if(isShowing()){
            this.dismiss();
        }
        return this;
    }

}