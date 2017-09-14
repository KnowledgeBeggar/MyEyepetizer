package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/11.
 */

public class FoundCategoryItem {

    @NonNull public Data data;

    public FoundCategoryItem(@NonNull Data data) {
        this.data = data;
    }
}
