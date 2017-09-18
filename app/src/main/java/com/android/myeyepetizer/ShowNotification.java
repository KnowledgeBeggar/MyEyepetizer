package com.android.myeyepetizer;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Administrator on 2017/9/18.
 */

public class ShowNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() != Activity.RESULT_OK) {
            return;
        }

        int requestCode = intent.getIntExtra(UpdateService.CODE_REQUEST, 0);
        Notification notification = (Notification) intent.getParcelableExtra(UpdateService.NOTIFICATION);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(requestCode, notification);
    }
}
