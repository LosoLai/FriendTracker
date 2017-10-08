package com.example.loso.friendtracker.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;

import com.example.loso.friendtracker.Controller.PreferenceController;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.R;
import com.example.loso.friendtracker.Service.MeetingSuggestionController;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Loso on 2017/10/2.
 */

public class AlarmSuggestionReceiver extends BroadcastReceiver {
    public static final int ALARM_SUGGESTION_ID = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceController preferenceController = PreferenceController.getInstance();
        MeetingSuggestionController meetingSuggestionController = MeetingSuggestionController.getInstance();


        SharedPreferences pref = context.getSharedPreferences("location", Context.MODE_PRIVATE);
        LatLng here = new LatLng(pref.getFloat("latitude", (float) Location.RMIT.getLatitude()),
                pref.getFloat("longitude", (float) Location.RMIT.getLongitude()));
        Location currentLocation = new Location(here);


        Meeting suggest;
//        if(meetingSuggestionController.getStatus() == MeetingSuggestionController.INITIAL)
        suggest = meetingSuggestionController.createASuggestedMeeting(currentLocation);
//        else
//            suggest = meetingSuggestionController.getSuggestion();

        if(suggest == null)
            return;

//        Intent settingIntent = new Intent(context, UserSettingActivity.class);
//        PendingIntent preSetting = PendingIntent.getActivity(context, 0, settingIntent, 0);

        Intent cancleIntent = new Intent(context, ActionCancelSuggestionActivity.class);
        PendingIntent actionCancel = PendingIntent.getActivity(context, ALARM_SUGGESTION_ID, cancleIntent, 0);

        Intent cleanIntent = new Intent(context, ActionCleanSuggestionActivity.class);
        PendingIntent actionNo = PendingIntent.getActivity(context, ALARM_SUGGESTION_ID, cleanIntent, 0);

        Intent acceptIntent = new Intent(context, ActionAcceptSuggestionActivity.class);
        PendingIntent actionYes = PendingIntent.getActivity(context, ALARM_SUGGESTION_ID, acceptIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .addAction(R.mipmap.ic_launcher_round, "YES", actionYes)
                .addAction(R.mipmap.ic_launcher_round, "NO", actionNo)
                .addAction(R.mipmap.ic_launcher_round, "CANCEL", actionCancel)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setContentIntent(preSetting)
                .setContentTitle("Meeting Suggestion")
                .setContentText(suggest.toString())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ALARM_SUGGESTION_ID, builder.build());
    }
}
