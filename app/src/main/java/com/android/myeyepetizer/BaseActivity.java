package com.android.myeyepetizer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {

    private NetworkStateReceiver mStateReceiver;
    private NotificationReceiver mNotificationReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mStateReceiver = new NetworkStateReceiver();
        registerReceiver(mStateReceiver, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(UpdateService.ACTION_SHOW_NOTIFICATION);
        intentFilter1.setPriority(100);
        mNotificationReceiver = new NotificationReceiver();
        registerReceiver(mNotificationReceiver, intentFilter1, UpdateService.PERM_PRIVATE, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStateReceiver != null) {
            unregisterReceiver(mStateReceiver);
            mStateReceiver = null;
        }
        if (mNotificationReceiver != null) {
            unregisterReceiver(mNotificationReceiver);
            mNotificationReceiver = null;
        }
    }

    class NetworkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isInitialStickyBroadcast()) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        Toast.makeText(BaseActivity.this, "Wifi已连接请尽情使用", Toast.LENGTH_SHORT).show();
                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Toast.makeText(BaseActivity.this, "当前为数据连接，请注意流量", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BaseActivity.this, "当前网络不可用，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
          setResultCode(Activity.RESULT_CANCELED);
        }
    }

}
