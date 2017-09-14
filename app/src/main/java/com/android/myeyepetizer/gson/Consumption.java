package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/4.
 */

public class Consumption implements Parcelable{

    public int collectionCount;

    public int shareCount;

    public int replyCount;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.collectionCount);
        dest.writeInt(this.shareCount);
        dest.writeInt(this.replyCount);
    }

    public Consumption() {
    }

    protected Consumption(Parcel in) {
        this.collectionCount = in.readInt();
        this.shareCount = in.readInt();
        this.replyCount = in.readInt();
    }

    public static final Creator<Consumption> CREATOR = new Creator<Consumption>() {
        @Override
        public Consumption createFromParcel(Parcel source) {
            return new Consumption(source);
        }

        @Override
        public Consumption[] newArray(int size) {
            return new Consumption[size];
        }
    };
}