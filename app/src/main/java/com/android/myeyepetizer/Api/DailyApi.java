package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Issue;
import com.android.myeyepetizer.gson.IssueDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/6.
 */

public interface DailyApi {

    @GET("v2/feed?issueIndex=1")
    Observable<IssueDataBean> getDailyItem();

    @GET
    Observable<IssueDataBean> loadMoreDailyItem(@Url String url);

}
