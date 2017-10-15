package com.example.loso.friendtracker.Controller;

import android.content.SharedPreferences;

import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.View.UserSettingActivity;

/**
 * Created by Loso on 2017/10/8.
 */

public class PreferenceController {
    private static PreferenceController instance = null;
    private Location currentLocation;
    private boolean reminderFlag;
    private boolean suggestionFlag;
    private int reminderTime;
    private int snooze;
    private int suggestion;
    private boolean networkFlag;

    public static PreferenceController getInstance()
    {
        if(instance == null)
            instance = new PreferenceController();

        return instance;
    }

    public boolean isReminderFlag() {
        return reminderFlag;
    }

    public void setReminderFlag(boolean reminderFlag) {
        this.reminderFlag = reminderFlag;
    }

    public boolean isSuggestionFlag() {
        return suggestionFlag;
    }

    public void setSuggestionFlag(boolean suggestionFlag) {
        this.suggestionFlag = suggestionFlag;
    }

    public int getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(int reminderTime) {
        this.reminderTime = reminderTime;
    }

    public int getSnooze() {
        return snooze;
    }

    public void setSnooze(int snooze) {
        this.snooze = snooze;
    }

    public int getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(int suggestion) {
        this.suggestion = suggestion;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isNetworkFlag() {
        return networkFlag;
    }

    public void setNetworkFlag(boolean networkFlag) {
        this.networkFlag = networkFlag;
    }

    public void loadSharedPreference(final SharedPreferences prefs)
    {
        //force remider to be flase
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(UserSettingActivity.MyPreferenceFragment.MEETING_NOTIFICATION, false);
        editor.apply();
        setReminderFlag(false);

        //force suggest to be flase
        editor.putBoolean(UserSettingActivity.MyPreferenceFragment.MEETING_SUGGESTION, false);
        editor.apply();
        setSuggestionFlag(false);

        String time = prefs.getString(UserSettingActivity.MyPreferenceFragment.MEETING_NOTIFICATION_TIME, "3");
        int remainderTime = Integer.parseInt(time);
        setReminderTime(remainderTime);
        time = prefs.getString(UserSettingActivity.MyPreferenceFragment.MEETING_NOTIFICATION_SNOOZE, "1");
        int snooze = Integer.parseInt(time);
        setSnooze(snooze);
        time = prefs.getString(UserSettingActivity.MyPreferenceFragment.MEETING_SUGGESTION_TIME, "30");
        int suggestion = Integer.parseInt(time);
        setSuggestion(suggestion);
    }
}
