package com.example.loso.friendtracker.View;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

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
        private static final String MEETING_NOTIFICATION = "pref_meeting_notify_flag";
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("pref_meeting_notify_flag");
            checkBoxPreference.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean checked = Boolean.valueOf(newValue.toString());
                    if(checked)
                    {
                        // fire alarm
                        Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // cancle alarm
                        Toast.makeText(getActivity(), "Unchecked", Toast.LENGTH_SHORT).show();
                    }
                    checkBoxPreference.setChecked(checked);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(MEETING_NOTIFICATION, checked);
                    editor.commit();
                    return true;
                }
            });

            //check alarm shuld be fire or not
            AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), AlarmNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            if (checkBoxPreference.isChecked())
            {
                if(alarmManager != null)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000, 3000, pendingIntent);
            }
            else
            {
                if(alarmManager != null)
                    alarmManager.cancel(pendingIntent);
            }
        }
    }
}
