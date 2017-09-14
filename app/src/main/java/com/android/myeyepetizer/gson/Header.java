package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/9/6.
 */

public class Header implements Parcelable{

    public String cover;
    public String title;
    public String subTitle;
    public String icon;
    public String description;
    public int id;
    public boolean ifPgc;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cover);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.icon);
        dest.writeString(this.description);
        dest.writeInt(this.id);
        dest.writeByte(this.ifPgc ? (byte) 1 : (byte) 0);
    }

    public Header() {
    }

    protected Header(Parcel in) {
        this.cover = in.readString();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.icon = in.readString();
        this.description = in.readString();
        this.id = in.readInt();
        this.ifPgc = in.readByte() != 0;
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel source) {
            return new Header(source);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };
}
