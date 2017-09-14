package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/8/31.
 */

public class MovieInfo {

    @NonNull public String title;
    @NonNull public String category;
    @NonNull public String description;
    @NonNull public int duration;
    @NonNull public int collectionCount;
    @NonNull public int shareCount;
    @NonNull public int replyCount;
    @NonNull public boolean isStartAnimation;

    public MovieInfo(@NonNull String title,
                     @NonNull String category,
                     @NonNull String description,
                     @NonNull int duration,
                     @NonNull int collectionCount,
                     @NonNull int shareCount,
                     @NonNull int replyCount,
                     @NonNull boolean isStartAnimation) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.duration = duration;
        this.collectionCount = collectionCount;
        this.shareCount = shareCount;
        this.replyCount = replyCount;
        this.isStartAnimation = isStartAnimation;
    }
}
