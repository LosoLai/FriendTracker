package com.example.loso.friendtracker.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Lettisia George on 01/10/2017.
 */

public class NetworkStatusReceiver extends BroadcastReceiver {
    public static final String NETWORK_CHANGE_DETECTED = "NetworkChanged";
    public static final String IS_NETWORK_CONNECTED = "isNetworkConnected";
    private static final String LOG_TAG = "NetworkStatusReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        Intent networkStatus = new Intent(NETWORK_CHANGE_DETECTED);
        networkStatus.putExtra(IS_NETWORK_CONNECTED, isConnected);
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStatus);

        Log.d(LOG_TAG, isConnected ? "connected" : "disconnected");
    }
}
