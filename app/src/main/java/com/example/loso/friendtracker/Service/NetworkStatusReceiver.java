package com.example.loso.friendtracker.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.loso.friendtracker.Alarm.MeetingSuggestionManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Lettisia George on 01/10/2017.
 */

public class NetworkStatusReceiver extends BroadcastReceiver {
    public static final String NETWORK_CHANGE_DETECTED = "NetworkChanged";
    public static final String IS_NETWORK_CONNECTED = "isNetworkConnected";
    private static final String LOG_TAG = "NetworkStatusReceiver";
    private static boolean isFirstConnect = true;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "entered onReceive()");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected;
        MeetingSuggestionManager meetingSuggestionManager = MeetingSuggestionManager.getInstance();
        if (networkInfo != null) {
            if (isFirstConnect) {
                isConnected = networkInfo.isConnected();
                Intent networkStatus = new Intent(NETWORK_CHANGE_DETECTED);
                networkStatus.putExtra(IS_NETWORK_CONNECTED, isConnected);
                LocalBroadcastManager.getInstance(context).sendBroadcast(networkStatus);
                Log.i(LOG_TAG, isConnected ? "connected" : "disconnected");
                isFirstConnect = false;
                //active suggestion
                meetingSuggestionManager.enableSuggestionByNetworkStatus();
            }
        } else {
            isFirstConnect = true;
            meetingSuggestionManager.disableSuggestionByNetworkStatus();
        }
    }
}
