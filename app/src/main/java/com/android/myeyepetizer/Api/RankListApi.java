package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.GetDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface RankListApi {

    @GET("v3/ranklist?strategy=weekly")
    Observable<GetDataBean> getWeekRankList();

    @GET("v3/ranklist?strategy=monthly")
    Observable<GetDataBean> getMonthRankList();

    @GET("v3/ranklist?strategy=historical")
    Observable<GetDataBean> getHistoricalList();

}
