package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */

public class Item implements Parcelable{

    public String type;

    public Data data;

    public String tag;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.tag);
    }

    public Item() {
    }

    protected Item(Parcel in) {
        this.type = in.readString();
        this.data = in.readParcelable(Data.class.getClassLoader());
        this.tag = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
