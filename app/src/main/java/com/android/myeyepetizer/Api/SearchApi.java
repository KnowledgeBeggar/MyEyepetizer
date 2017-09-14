package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.GetDataBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface SearchApi {

    @GET("v3/queries/hot")
    Observable<List<String>> getHotTags();

    @GET("v1/search")
    Observable<GetDataBean> searchTagData(@Query("query") String tag);

    @GET
    Observable<GetDataBean> loadMoreData(@Url String url);

}
