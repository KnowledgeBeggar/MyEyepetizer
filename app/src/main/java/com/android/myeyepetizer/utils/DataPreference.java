package com.android.myeyepetizer.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2017/9/17.
 */

public class DataPreference {

    private static final String PREF_HOME_DATA = "homeData";
    private static final String PREF_HOME_DATE = "homeDate";
    private static final String PREF_FOUND_DATA = "foundData";
    private static final String PREF_FOLLOW_DATA = "followData";

    public static void setLastPrefHomeData(Context context, String data) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_HOME_DATA, data)
                .apply();
    }

    public static String getLastPrefHomeData(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_HOME_DATA, null);
    }

    public static void setLastPrefHomeDate(Context context, long date) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_HOME_DATE, date)
                .apply();
    }

    public static long getLastPrefHomeDate(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(PREF_HOME_DATE, 0L);
    }

    public static void setLastPrefFoundData(Context context, String data) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_FOUND_DATA, data)
                .apply();
    }

    public static String getLastPrefFoundData(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_FOUND_DATA, null);
    }

    public static void setLastPrefFollowData(Context context, String data) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_FOLLOW_DATA, data)
                .apply();
    }

    public static String getLastPrefFollowData(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_FOLLOW_DATA, null);
    }

}
