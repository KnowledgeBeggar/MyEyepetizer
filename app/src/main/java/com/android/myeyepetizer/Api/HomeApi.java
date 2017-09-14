package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.GetDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/30.
 */

public interface HomeApi {

    @GET("v4/tabs/selected")
    Observable<GetDataBean> getHomeItem();

    @GET
    Observable<GetDataBean> loadMoreItem(@Url String url);

}
