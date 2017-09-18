package com.android.myeyepetizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = UpdateService.newIntent(context);
        context.startService(i);
    }
}
