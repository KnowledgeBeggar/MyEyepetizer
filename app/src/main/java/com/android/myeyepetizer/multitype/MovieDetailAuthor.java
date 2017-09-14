package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

public class MovieDetailAuthor {

    @NonNull public String icon;
    @NonNull public String name;
    @NonNull public String description;
    @NonNull public int id;

    public MovieDetailAuthor(@NonNull String icon, @NonNull String name, @NonNull String description, @NonNull int id) {
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.id = id;
    }
}
