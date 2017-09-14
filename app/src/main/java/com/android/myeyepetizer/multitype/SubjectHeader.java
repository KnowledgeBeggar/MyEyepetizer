package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/9/8.
 */

public class SubjectHeader {

    @NonNull public String imageUri;
    @NonNull public String brief;
    @NonNull public String text;

    public SubjectHeader(@NonNull String imageUri, @NonNull String brief, @NonNull String text) {
        this.imageUri = imageUri;
        this.brief = brief;
        this.text = text;
    }
}
