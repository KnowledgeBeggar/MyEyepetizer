package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/4.
 */

public class Author implements Parcelable{

    public String icon;

    public String name;

    public String description;

    public int id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.id);
    }

    public Author() {
    }

    protected Author(Parcel in) {
        this.icon = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
}
