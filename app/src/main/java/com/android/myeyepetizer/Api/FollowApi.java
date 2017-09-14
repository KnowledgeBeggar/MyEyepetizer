package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.GetDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface FollowApi {

    @GET("v4/tabs/follow")
    Observable<GetDataBean> getFollowData();

    @GET
    Observable<GetDataBean> loadMoreData(@Url String url);

}
