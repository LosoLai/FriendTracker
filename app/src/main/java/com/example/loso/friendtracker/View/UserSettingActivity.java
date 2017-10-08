package com.example.loso.friendtracker.View;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.loso.friendtracker.Alarm.AlarmNotificationReceiver;
import com.example.loso.friendtracker.Alarm.AlarmSuggestionReceiver;
import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Controller.PreferenceController;
import com.example.loso.friendtracker.R;

/**
 * Created by Loso on 2017/10/1.
 */

public class UserSettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        public static final String MEETING_NOTIFICATION = "pref_meeting_notify_flag";
        public static final String MEETING_NOTIFICATION_TIME = "pref_meeting_notify_time_limit";
        public static final String MEETING_NOTIFICATION_SNOOZE = "pref_meeting_notify_snooze_time_limit";
        public static final String MEETING_SUGGESTION = "pref_meeting_suggestion_flag";
        public static final String MEETING_SUGGESTION_TIME = "pref_meeting_suggestion_time_limit";

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            final SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            final PreferenceController preferenceController = PreferenceController.getInstance();

            setMeetingNotification(prefs, preferenceController);
            setMeetingSuggestion(prefs, preferenceController);
        }

        public void setMeetingNotification(final SharedPreferences prefs, final PreferenceController preferenceController)
        {
            final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(MEETING_SUGGESTION);
            checkBoxPreference.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean checked = Boolean.valueOf(newValue.toString());
                    preferenceController.setReminderFlag(checked);

                    //check alarm shuld be fire or not
                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getActivity(), AlarmNotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), AlarmNotificationReceiver.ALARM_NOTIFY_ID, intent, 0);

                    //get upcomming meeting info
                    MeetingController meetingController = new MeetingController();
                    long meetingTime = meetingController.getUpcommingMeeting().getStartDate().getTime();
                    //get time limit value
                    String time = prefs.getString(MEETING_NOTIFICATION_TIME, "9");
                    int duration = Integer.valueOf(time) * 60 * 1000;
                    long reminderTime = meetingTime - duration;

                    if(checked) {
                        if(alarmManager != null)
                            //test
                            //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pendingIntent);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                        Toast.makeText(getActivity(), "Active reminder", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(alarmManager != null)
                            alarmManager.cancel(pendingIntent);
                        Toast.makeText(getActivity(), "Inactive reminder", Toast.LENGTH_SHORT).show();
                    }

                    checkBoxPreference.setChecked(checked);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(MEETING_NOTIFICATION, checked);
                    editor.apply();
                    return true;
                }
            });

            final EditTextPreference editTextPreference = (EditTextPreference) findPreference(MEETING_NOTIFICATION_TIME);
            editTextPreference.setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int time = Integer.parseInt(newValue.toString());
                    preferenceController.setReminderTime(time);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(MEETING_NOTIFICATION_TIME, Integer.toString(time));
                    editor.apply();
                    return true;
                }
            });
        }

        public void setMeetingSuggestion(final SharedPreferences prefs, final PreferenceController preferenceController)
        {
            final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(MEETING_SUGGESTION);
            checkBoxPreference.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean checked = Boolean.valueOf(newValue.toString());
                    preferenceController.setSuggestionFlag(checked);

                    //check alarm shuld be fire or not
                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getActivity(), AlarmSuggestionReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), AlarmSuggestionReceiver.ALARM_SUGGESTION_ID, intent, 0);

                    //get time limit
                    int time = Integer.valueOf(prefs.getString(MEETING_SUGGESTION_TIME, "30")) * 1000;

                    if(checked) {
                        if(alarmManager != null)
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+time, time, pendingIntent);
                        Toast.makeText(getActivity(), "Suggestion Active", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(alarmManager != null)
                            alarmManager.cancel(pendingIntent);
                        Toast.makeText(getActivity(), "Suggestion Inactive", Toast.LENGTH_SHORT).show();
                    }

                    checkBoxPreference.setChecked(checked);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(MEETING_SUGGESTION, checked);
                    editor.apply();
                    return true;
                }
            });
        }
    }
}
