package com.danqiu.online.edu.base;

import android.content.Context;

import java.lang.ref.SoftReference;

/**
 * Created by lzj on 2019/10/21
 * Describe ：注释
 */
public class BaseMvpPresenter<V extends IBaseView> implements IBasePresenter<V>{
    private SoftReference<IBaseView> mReferenceView;
    /**
     * 绑定view
     * @param view
     */
    @Override
    public void attachView(V view) {
        this.mReferenceView = new SoftReference<>(view);
    }

    /**
     * 解绑view
     */
    @Override
    public void detachView() {
        mReferenceView.clear();
        mReferenceView = null;
    }

    /**
     * 判断view
     */
    @SuppressWarnings("unchecked")
    @Override
    public V getView() {
        if(mReferenceView!=null){
            return (V) mReferenceView.get();
        }
        else {
            return null;
        }
    }

    /**
     * 获取上下文
     */
    public Context getContext() {
        return getView().getContext();
    }

     /*  protected CompositeSubscription mCompositeSubscription;

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
    //RXjava注册
    public void addSubscription(Subscription subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscriber);
    }*/
}
