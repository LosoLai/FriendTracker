package com.example.loso.friendtracker.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Service.MeetingSuggestionController;

/**
 * Created by Loso on 2017/10/7.
 */

public class ActionAcceptSuggestionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MeetingSuggestionController meetingSuggestionController = MeetingSuggestionController.getInstance();
        Meeting suggestion = meetingSuggestionController.getSuggestion();

        MeetingController meetingController = new MeetingController();
        meetingController.addMeetingIntoList(suggestion);
        meetingSuggestionController.setStatus(MeetingSuggestionController.YES);

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmSuggestionReceiver.ALARM_SUGGESTION_ID);
        finish();
    }
}
