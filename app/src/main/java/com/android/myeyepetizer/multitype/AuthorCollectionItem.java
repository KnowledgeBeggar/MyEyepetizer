package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/12.
 */

public class AuthorCollectionItem {

    @NonNull public Data data;
    @NonNull public int type;

    public AuthorCollectionItem(@NonNull Data data, @NonNull int type) {
        this.data = data;
        this.type = type;
    }
}
