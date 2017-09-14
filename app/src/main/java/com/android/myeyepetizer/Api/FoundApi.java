package com.android.myeyepetizer.Api;

import android.net.Uri;

import com.android.myeyepetizer.gson.Discovery;
import com.android.myeyepetizer.gson.GetDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface FoundApi {

    @GET("v4/discovery")
    Observable<Discovery> getDiscovery();

    @GET
    Observable<GetDataBean> getFoundCategoryData(@Url String url);

}
