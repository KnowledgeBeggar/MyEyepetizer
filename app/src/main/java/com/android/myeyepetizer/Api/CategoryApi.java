package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.CategoryDataBean;
import com.android.myeyepetizer.gson.GetDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/8.
 */

public interface CategoryApi {

    @GET("v4/categories/detail/tab")
    Observable<CategoryDataBean> getCategoryData(@Query("id") int id);

    @GET("v4/categories/all")
    Observable<GetDataBean> getAllCategoryData();

    @GET
    Observable<GetDataBean> loadDetailCategoryData(@Url String url);

}
