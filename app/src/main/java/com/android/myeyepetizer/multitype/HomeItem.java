package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/5.
 */

public class HomeItem {

    @NonNull public Data data;

    public HomeItem(@NonNull Data data) {
        this.data = data;
    }
}
