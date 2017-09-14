package com.android.myeyepetizer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/30.
 */

public class WorkerRetrofit {

    private static final String BASE_URL = "http://baobab.kaiyanapp.com/api/";

    private Map<Class, Object> mApis = new HashMap<>();

    private Retrofit mRetrofit;

    public WorkerRetrofit() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(25, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public <T>T createApi(Class<T> service) {
        if (!mApis.containsKey(service)) {
            T instance = mRetrofit.create(service);
            mApis.put(service, instance);
        }

        return (T) mApis.get(service);
    }

}
