package com.android.myeyepetizer.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */
public class Data implements Parcelable{

    public String dataType;
    public int id;
    public String text;
    public String image;
    public String icon;
    public String title;
    public String slogan;
    public String description;
    public String category;
    public Provider provider;
    public Author author;
    public Cover cover;
    public String playUrl;
    public String thumbPlayUrl;
    public int duration;
    public List<PlayInfo> playInfo;
    public Consumption consumption;
    public List<Item> itemList;
    public Header header;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dataType);
        dest.writeInt(this.id);
        dest.writeString(this.text);
        dest.writeString(this.image);
        dest.writeString(this.icon);
        dest.writeString(this.title);
        dest.writeString(this.slogan);
        dest.writeString(this.description);
        dest.writeString(this.category);
        dest.writeParcelable(this.provider, flags);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.cover, flags);
        dest.writeString(this.playUrl);
        dest.writeString(this.thumbPlayUrl);
        dest.writeInt(this.duration);
        dest.writeTypedList(this.playInfo);
        dest.writeParcelable(this.consumption, flags);
        dest.writeTypedList(this.itemList);
        dest.writeParcelable(this.header, flags);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.dataType = in.readString();
        this.id = in.readInt();
        this.text = in.readString();
        this.image = in.readString();
        this.icon = in.readString();
        this.title = in.readString();
        this.slogan = in.readString();
        this.description = in.readString();
        this.category = in.readString();
        this.provider = in.readParcelable(Provider.class.getClassLoader());
        this.author = in.readParcelable(Author.class.getClassLoader());
        this.cover = in.readParcelable(Cover.class.getClassLoader());
        this.playUrl = in.readString();
        this.thumbPlayUrl = in.readString();
        this.duration = in.readInt();
        this.playInfo = in.createTypedArrayList(PlayInfo.CREATOR);
        this.consumption = in.readParcelable(Consumption.class.getClassLoader());
        this.itemList = in.createTypedArrayList(Item.CREATOR);
        this.header = in.readParcelable(Header.class.getClassLoader());
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}