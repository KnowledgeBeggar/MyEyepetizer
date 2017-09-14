package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/5.
 */

public class Banner {

    @NonNull
    public Data data;

    public int intentFlag;//决定squareCardBinder启动哪个activity

    public int type;

    public Banner(@NonNull Data data) {
        this.data = data;
    }

    public Banner(@NonNull Data data, int intentFlag) {
        this.data = data;
        this.intentFlag = intentFlag;
    }

    public Banner(@NonNull Data data, int intentFlag, int type) {
        this.data = data;
        this.intentFlag = intentFlag;
        this.type = type;
    }
}
