package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;

import com.android.myeyepetizer.gson.Reply;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ReplyInfoItem {

    @NonNull public Reply reply;

    public ReplyInfoItem(@NonNull Reply reply) {
        this.reply = reply;
    }
}
