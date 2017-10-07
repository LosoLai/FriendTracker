package com.example.loso.friendtracker.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.loso.friendtracker.R;
import com.example.loso.friendtracker.View.ActionCancleSuggestionActivity;
import com.example.loso.friendtracker.View.UserSettingActivity;

/**
 * Created by Loso on 2017/10/2.
 */

public class AlarmSuggestionReceiver extends BroadcastReceiver {
    public static final int ALARM_SUGGESTION_ID = 2;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent settingIntent = new Intent(context, UserSettingActivity.class);
        PendingIntent preSetting = PendingIntent.getActivity(context, 0, settingIntent, 0);

        Intent cancleIntent = new Intent(context, ActionCancleSuggestionActivity.class);
        PendingIntent actionCancle = PendingIntent.getActivity(context, ALARM_SUGGESTION_ID, cancleIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .addAction(R.drawable.common_google_signin_btn_icon_light_normal, "NO", null)
                .addAction(R.drawable.common_google_signin_btn_icon_light_normal, "CANCLE", actionCancle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(preSetting)
                .setContentTitle("Alarm Actived!")
                .setContentText("This is a meeting suggestion.")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ALARM_SUGGESTION_ID, builder.build());
    }
}
