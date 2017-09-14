package com.android.myeyepetizer.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/9/11.
 */

public class TranslateDptoPiexel {
    public static int translate(int dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dp * scale + 0.5f);
    }
}
