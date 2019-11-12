package com.danqiu.online.edu.retrofit;

import com.danqiu.online.edu.utils.L;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by lzj on 2019/10/22
 * Describe ：观察者
 */
public abstract class ProgressSubscriber <T> extends Subscriber<T> {
    private String t="test";

    public ProgressSubscriber() {

    }
    @Override
    public void onCompleted() {
        L.i(t,"-----------------ww");
        _onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        //_onCompleted();
        L.i(t,"------------onError:"+e.getMessage());

        if(e instanceof UnknownHostException) {
            _onError("网络错误!");
        }
        else if (e instanceof SocketTimeoutException){
            _onError("网络连接超时!");
        }
        else if (e instanceof ConnectException) {
            _onError("网络连接失败!");
        }
        else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int responseCode = httpException.code();
            if (responseCode >= 400 && responseCode <= 417) {
                _onError("访问地址异常,请稍后重试!");
            } else if (responseCode >= 500 && responseCode <= 505) {
                _onError("服务器繁忙!");
            } else {
                _onError("网络连接异常!");
            }
            _onError("服务器异常");
        }
        else {
            _onError(e.getMessage());
        }
    }
    @Override
    public void onNext(T t) {
        //_onCompleted();
        _onNext(t);
    }

    protected abstract void _onNext(Object object);

    protected abstract void _onError(String message);

    protected abstract void _onCompleted();


}
