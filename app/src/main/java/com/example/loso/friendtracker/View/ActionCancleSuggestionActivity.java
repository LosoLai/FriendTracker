package com.example.loso.friendtracker.View;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.loso.friendtracker.Service.AlarmNotificationReceiver;
import com.example.loso.friendtracker.Service.AlarmSuggestionReceiver;

/**
 * Created by Loso on 2017/10/7.
 */

public class ActionCancleSuggestionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, AlarmSuggestionReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmSuggestionReceiver.ALARM_SUGGESTION_ID, intent, 0);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }
}
