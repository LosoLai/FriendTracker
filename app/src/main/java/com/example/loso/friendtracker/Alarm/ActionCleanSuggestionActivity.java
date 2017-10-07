package com.example.loso.friendtracker.Alarm;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Service.MeetingSuggestionController;

/**
 * Created by Loso on 2017/10/7.
 */

public class ActionCleanSuggestionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmSuggestionReceiver.ALARM_SUGGESTION_ID);
        finish();
    }
}
