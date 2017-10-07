package com.example.loso.friendtracker.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Loso on 2017/10/7.
 */

public class ActionCancelSuggestionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, AlarmSuggestionReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmSuggestionReceiver.ALARM_SUGGESTION_ID, intent, 0);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmSuggestionReceiver.ALARM_SUGGESTION_ID);
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }
}
