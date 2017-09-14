package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.android.myeyepetizer.gson.Data;
import com.android.myeyepetizer.gson.Item;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class HomeTopPager {

    @NonNull public List<Data> dataList;
    @NonNull public FragmentManager manager;
    @NonNull public Activity activity;

    public HomeTopPager(List<Data> dataList, FragmentManager manager, Activity activity) {
        this.dataList = dataList;
        this.manager = manager;
        this.activity = activity;
    }

}
