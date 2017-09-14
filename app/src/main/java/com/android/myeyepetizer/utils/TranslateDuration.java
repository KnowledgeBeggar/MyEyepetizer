package com.android.myeyepetizer.utils;

/**
 * Created by Administrator on 2017/8/31.
 */

public class TranslateDuration {

    public static String translateSeconds(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;

        if ( (minutes / 10) == 0) {
            return "0" + minutes + "'" + seconds + "''";
        } else {
            return minutes + "'" + seconds + "''";
        }
    }

}
