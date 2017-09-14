package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.AuthorDataBean;
import com.android.myeyepetizer.gson.Data;
import com.android.myeyepetizer.gson.GetDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface AuthorApi {

    @GET("v4/pgcs/detail/tab")
    Observable<AuthorDataBean> getAuthorData(@Query("id") int id);

    @GET
    Observable<GetDataBean> loadMoreData(@Url String url);

}
