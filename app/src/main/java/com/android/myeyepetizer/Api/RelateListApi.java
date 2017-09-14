package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.GetDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface RelateListApi {

    @GET("v4/video/related?")
    Observable<GetDataBean> getRelatedData(@Query("id") int id);

}
