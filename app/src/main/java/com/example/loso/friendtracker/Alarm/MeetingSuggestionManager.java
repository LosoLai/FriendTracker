package com.example.loso.friendtracker.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Controller.PreferenceController;
import com.example.loso.friendtracker.Model.Meeting;

import java.util.Calendar;

/**
 * Created by Loso on 2017/10/15.
 */

public class MeetingSuggestionManager {
    private static MeetingSuggestionManager instance;
    private MeetingController meetingController = new MeetingController();
    private PreferenceController preferenceController = PreferenceController.getInstance();
    private Intent intent;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private boolean isSet;

    public static MeetingSuggestionManager getInstance()
    {
        if(instance == null)
            instance = new MeetingSuggestionManager();

        return instance;
    }

    public void enableMeetingSuggestion(AlarmManager alarmManager, Intent intent, PendingIntent pendingIntent) {
        this.alarmManager = alarmManager;
        this.intent = intent;
        this.pendingIntent = pendingIntent;

        int time = preferenceController.getSuggestion() * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+time, time, pendingIntent);
        preferenceController.setSuggestionFlag(true);
    }

    public void enableSuggestionByNetworkStatus()
    {
        int time = preferenceController.getSuggestion() * 1000;
        if(alarmManager != null && intent != null && pendingIntent != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + time, time, pendingIntent);
            preferenceController.setSuggestionFlag(true);
            isSet = false;
        }
    }

    public void disableMeetingSuggestion(AlarmManager alarmManager, PendingIntent pendingIntent)
    {
        alarmManager.cancel(pendingIntent);
        preferenceController.setSuggestionFlag(false);
    }

    public void disableSuggestionByNetworkStatus()
    {
        if(!isSet)
        {
            if(alarmManager != null && pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                preferenceController.setSuggestionFlag(false);
                isSet = true;
            }
        }
    }
}
