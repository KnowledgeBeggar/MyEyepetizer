package com.android.myeyepetizer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import com.android.myeyepetizer.Api.FollowApi;
import com.android.myeyepetizer.Api.HomeApi;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.utils.DataPreference;
import com.google.gson.Gson;

import java.util.Observer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class UpdateService extends IntentService {

    public static final String ACTION_SHOW_NOTIFICATION = "com.android.myeyepetizer.SHOW_NOTIFICATION";
    public static final String CODE_REQUEST = "requestCode";
    public static final String NOTIFICATION = "notification";
    public static final String PERM_PRIVATE = "com.android.myeyepetizer.PRIVATE";

    public static Intent newIntent(Context context) {
        return new Intent(context, UpdateService.class);
    }

    private HomeApi mHomeApi;
    private FollowApi mFollowApi;

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvaliableAndConnected()) {
            return;
        }

        final long homeDate = DataPreference.getLastPrefHomeDate(this);
        final String followData = DataPreference.getLastPrefFollowData(this);
        mHomeApi = RetrofitFactory.getRetrofit().createApi(HomeApi.class);
        mFollowApi = RetrofitFactory.getRetrofit().createApi(FollowApi.class);
        getDataByNet(homeDate, followData);
        setAlarmManager();
    }

    private void setAlarmManager() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = UpdateService.newIntent(this);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_HALF_DAY, pi);
    }

    private void getDataByNet(final long homeDate, final String followData) {
        Observable<GetDataBean> homeOb = mHomeApi.getHomeItem();
        homeOb
                .filter(new Predicate<GetDataBean>() {
                    @Override
                    public boolean test(@NonNull GetDataBean getDataBean) throws Exception {
                        return getDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetDataBean>() {
                    @Override
                    public void accept(GetDataBean getDataBean) throws Exception {
                        if (homeDate != getDataBean.date) {
                            DataPreference.setLastPrefHomeData(UpdateService.this, new Gson().toJson(getDataBean));
                            DataPreference.setLastPrefHomeDate(UpdateService.this, getDataBean.date);
                        }
                    }
                });
        Observable<GetDataBean> followOb = mFollowApi.getFollowData();
        followOb
                .filter(new Predicate<GetDataBean>() {
                    @Override
                    public boolean test(@NonNull GetDataBean getDataBean) throws Exception {
                        return getDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetDataBean>() {
                    @Override
                    public void accept(GetDataBean getDataBean) throws Exception {
                        String data = new Gson().toJson(getDataBean);
                        if (!followData.equals(data)) {
                            DataPreference.setLastPrefFollowData(UpdateService.this, data);
                            initNotification(0);
                        }
                    }
                });
    }

    private void initNotification(int requestCode) {
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new NotificationCompat.Builder(UpdateService.this)
                .setContentTitle("开眼视频有新内容啦")
                .setContentText("开眼视频内容更新啦，快点进来看吧")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        showBackgroundNotification(requestCode, notification);
    }

    private boolean isNetworkAvaliableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isAvaliable = cm.getActiveNetworkInfo() != null;
        boolean isConnected =  isAvaliable && cm.getActiveNetworkInfo().isConnected();

        return isConnected;
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(CODE_REQUEST, requestCode);
        intent.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

}
