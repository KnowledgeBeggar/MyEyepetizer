package com.android.myeyepetizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/16.
 */

public class BaseActivity extends AppCompatActivity {

    private NetworkStateReceiver mStateReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mStateReceiver = new NetworkStateReceiver();
        registerReceiver(mStateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStateReceiver != null) {
            unregisterReceiver(mStateReceiver);
            mStateReceiver = null;
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

}
