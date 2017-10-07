package com.example.loso.friendtracker.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.loso.friendtracker.Controller.PreferenceController;

/**
 * Created by Loso on 2017/10/7.
 */

public class ActionSnoozeReminderActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmNotificationReceiver.ALARM_NOTIFY_ID, intent, 0);

        //get time limit value
        PreferenceController preferenceController = PreferenceController.getInstance();
        int snooze = preferenceController.getSnooze() * 60 * 1000;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
                + snooze, pendingIntent);
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }
}
