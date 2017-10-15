package com.example.loso.friendtracker.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.loso.friendtracker.Controller.PreferenceController;

/**
 * Created by Loso on 2017/10/7.
 */

public class ActionCancelReminderActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmNotificationReceiver.ALARM_NOTIFY_ID, intent, 0);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        PreferenceController preferenceController = PreferenceController.getInstance();
        preferenceController.setReminderFlag(false);
        //set sharepreferece
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("pref_meeting_notify_flag", false);
        editor.apply();

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmNotificationReceiver.ALARM_NOTIFY_ID);
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }
}
