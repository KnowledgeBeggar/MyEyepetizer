package com.android.myeyepetizer;


public class RetrofitFactory {

    private static final Object OBJECT = new Object();
    private static WorkerRetrofit retrofit;

    public static WorkerRetrofit getRetrofit() {
        synchronized (OBJECT) {
            if (retrofit == null) {
                retrofit = new WorkerRetrofit();
            }
            return retrofit;
        }
    }

}