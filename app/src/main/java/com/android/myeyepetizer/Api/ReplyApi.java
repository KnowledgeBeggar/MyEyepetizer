package com.android.myeyepetizer.Api;

import com.android.myeyepetizer.gson.ReplyDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/6.
 */

public interface ReplyApi {

    @GET("v1/replies/video")
    Observable<ReplyDataBean> getReplyData(@Query("id") int id);

    @GET
    Observable<ReplyDataBean> loadMoreReply(@Url String url);

}
