package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/5.
 */

public class Provider implements Parcelable{

    public String icon;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
    }

    public Provider() {
    }

    protected Provider(Parcel in) {
        this.icon = in.readString();
    }

    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel source) {
            return new Provider(source);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };
}
