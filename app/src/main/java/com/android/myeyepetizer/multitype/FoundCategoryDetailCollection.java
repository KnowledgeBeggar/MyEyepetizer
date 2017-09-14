package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/12.
 */

public class FoundCategoryDetailCollection {

    @NonNull public Data data;

    public FoundCategoryDetailCollection(@NonNull Data data) {
        this.data = data;
    }
}
