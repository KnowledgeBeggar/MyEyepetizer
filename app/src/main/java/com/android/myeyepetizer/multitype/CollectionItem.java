package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/6.
 */

public class CollectionItem {

    @NonNull
    public Data data;

    public int intentFlag;

    public CollectionItem(@NonNull Data data) {
        this.data = data;
    }

    public CollectionItem(@NonNull Data data, int intentFlag) {
        this.data = data;
        this.intentFlag = intentFlag;
    }
}
