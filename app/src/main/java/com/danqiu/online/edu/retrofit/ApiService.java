package com.danqiu.online.edu.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by lzj on 2019/10/22
 * Describe ：注释
 */
public interface ApiService {

    public static final String Base_URL = "http://www.danqiuedu.com/";//生产环境


    //  public static final String Base_URL = "http://uat.mstxx.com:90/uapi/";//测试环境

    // public static final String Base_URL = "http://10.0.255.82/m/";//开发环境

    // public static final String Base_URL = "http://10.0.255.82/m/";//开发环境2
    //public static final String Base_URL = "http://10.0.255.82:8080/m/";//开发环境2


    //   public static final String Base_URL = "http://10.0.0.118:9180/m/";//测试环境

    public static final String updateApp = Base_URL + "app/update";

    //登录
    @Headers("Content-type:application/json")
    @POST("login")
    Observable<ResponseBody> postLogin(@Body Map<String, String> map);

    @GET("uapi/api/pub/sysConfig/getVal")
    Observable<ResponseBody> getVersionCode(@Query("paramKey") String paramKey);

    @Streaming
    @GET
    Observable<ResponseBody> getDownloadApp(@Url String url);


}
