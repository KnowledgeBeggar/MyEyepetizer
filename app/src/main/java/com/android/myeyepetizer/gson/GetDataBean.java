package com.android.myeyepetizer.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataBean {

    public List<Item> itemList;

    public int count;

    public int total;

    public String nextPageUrl;

}
