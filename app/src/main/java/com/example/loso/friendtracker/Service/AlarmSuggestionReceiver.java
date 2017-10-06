package com.example.loso.friendtracker.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.loso.friendtracker.R;

/**
 * Created by Loso on 2017/10/2.
 */

public class AlarmSuggestionReceiver extends BroadcastReceiver {
    public static final int ALARM_SUGGESTION_ID = 2;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm Actived!")
                .setContentText("This is meeting suggestion.")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ALARM_SUGGESTION_ID, builder.build());
    }
}
