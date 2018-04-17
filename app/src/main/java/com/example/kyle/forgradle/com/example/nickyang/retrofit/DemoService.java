package com.example.kyle.forgradle.com.example.kyle.retrofit;

import com.example.kyle.forgradle.entity.GankRsp;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nick.yang on 2017/7/14.
 */

public interface DemoService {
    String BASE_URL = "http://gank.io/api/";

    @GET("data/Android/{size}/{index}")
    Call<ResponseBody> getAndroidInfo(@Path("size") int size, @Path("index") int index);

    @GET("data/Android/{size}/{index}")
    Observable<GankRsp> getAndroidInfoRx(@Path("size") int size, @Path("index") int index);
}
