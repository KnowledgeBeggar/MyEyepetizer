package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/4.
 */

public class PlayInfo implements Parcelable{

    public String name;

    public String type;

    public String url;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    public PlayInfo() {
    }

    protected PlayInfo(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Creator<PlayInfo> CREATOR = new Creator<PlayInfo>() {
        @Override
        public PlayInfo createFromParcel(Parcel source) {
            return new PlayInfo(source);
        }

        @Override
        public PlayInfo[] newArray(int size) {
            return new PlayInfo[size];
        }
    };
}