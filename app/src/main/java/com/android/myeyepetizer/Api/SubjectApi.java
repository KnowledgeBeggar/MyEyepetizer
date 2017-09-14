package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.WebPageData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/8.
 */

public interface SubjectApi {

    @GET("v3/lightTopics/webPage/{id}")
    Observable<WebPageData> getDetailSubject(@Path("id") int id);

    @GET("v3/specialTopics")
    Observable<GetDataBean> getHotSubject();

    @GET
    Observable<GetDataBean> loadMoreSubject(@Url String url);

}
