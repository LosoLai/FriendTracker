package com.example.loso.friendtracker.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.R;
import com.example.loso.friendtracker.View.UserSettingActivity;

/**
 * Created by Loso on 2017/10/2.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    public static final int ALARM_NOTIFY_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent settingIntent = new Intent(context, UserSettingActivity.class);
        PendingIntent preSetting = PendingIntent.getActivity(context, 0, settingIntent, 0);

        //get upcomming meeting info
        MeetingController meetingController = new MeetingController();
        String meetingInfo = meetingController.getUpcommingMeeting();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(preSetting)
                .setContentTitle("Meeting Reminder:")
                .setContentText(meetingInfo)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ALARM_NOTIFY_ID, builder.build());
    }
}
