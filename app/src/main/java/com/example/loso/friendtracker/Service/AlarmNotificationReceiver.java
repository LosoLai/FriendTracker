package com.example.loso.friendtracker.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import com.example.loso.friendtracker.R;

/**
 * Created by Loso on 2017/10/2.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    public static final int ALARM_NOTIFY_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm Actived!")
                .setContentText("This is meeting notification.")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ALARM_NOTIFY_ID, builder.build());
    }
}
