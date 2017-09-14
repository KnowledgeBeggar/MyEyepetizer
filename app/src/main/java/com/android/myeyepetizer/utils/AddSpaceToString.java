package com.android.myeyepetizer.utils;

/**
 * Created by Administrator on 2017/9/9.
 */

public class AddSpaceToString {

    public static String addSpace(String string) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            builder.append(string.charAt(i));
            builder.append(" ");
        }

        return builder.toString();
    }

}
