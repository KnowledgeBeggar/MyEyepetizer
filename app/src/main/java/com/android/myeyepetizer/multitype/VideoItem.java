package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Data;

/**
 * Created by Administrator on 2017/9/6.
 */

public class VideoItem {

    @NonNull public Data data;

    public VideoItem(@NonNull Data data) {
        this.data = data;
    }
}
