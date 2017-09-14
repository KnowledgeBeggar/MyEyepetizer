package com.android.myeyepetizer.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/7.
 */

public class TranslateDateFormat {

    public static String getDate(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(millisecond);

        return simpleDateFormat.format(date);
    }

}
