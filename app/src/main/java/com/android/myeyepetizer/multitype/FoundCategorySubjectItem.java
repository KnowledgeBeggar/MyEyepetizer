package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/9.
 */

public class FoundCategorySubjectItem {

    @NonNull public Data data;

    public FoundCategorySubjectItem(@NonNull Data data) {
        this.data = data;
    }
}
