package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/4.
 */


public class Cover implements Parcelable{

    @SerializedName("feed")
    public String feedCover;

    @SerializedName("detail")
    public String detailCover;

    @SerializedName("blurred")
    public String blurredCover;

    @SerializedName("homepage")
    public String homePageCover;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.feedCover);
        dest.writeString(this.detailCover);
        dest.writeString(this.blurredCover);
        dest.writeString(this.homePageCover);
    }

    public Cover() {
    }

    protected Cover(Parcel in) {
        this.feedCover = in.readString();
        this.detailCover = in.readString();
        this.blurredCover = in.readString();
        this.homePageCover = in.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel source) {
            return new Cover(source);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };
}