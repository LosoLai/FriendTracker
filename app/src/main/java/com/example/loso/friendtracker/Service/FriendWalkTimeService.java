package com.example.loso.friendtracker.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.WalkTime;

import java.util.ArrayList;


/**
 * CLass to automatically update the walk times for friends.
 * <p>
 * Created by Lettisia George on 29/09/2017.
 */

public class FriendWalkTimeService extends IntentService {
    private static final String LOG_TAG = "FriendWalkTimeService";
    private static final String SERVICE_NAME = "FriendWalkTimeService";

    private Location currentLocation = Location.RMIT;
    private FriendController friendController;
    private DistanceFinder distanceFinder;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FriendWalkTimeService() {
        super(SERVICE_NAME);
        friendController = new FriendController();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent()");
        ArrayList<Friend> friends = friendController.getFriendsList();
        for (Friend friend : friends) {
            generateFriendWalkTimes(friend);
        }
    }


    public void generateFriendWalkTimes(Friend friend) {
        String walkString = "";
        updateCurrentLocation();
        friendController.updateFriendLocations(getApplicationContext());
        distanceFinder = new DistanceFinder();

        Location friendLocation = friend.getLocation();
        if (friendLocation != null) {
            String url = distanceFinder.generateDistanceURL(currentLocation, friendLocation);
            Log.d(LOG_TAG, "URL: " + url);
            WalkTime walkTime = distanceFinder.parseWalkTime(distanceFinder.accessURL(url));
            friendController.setWalkTime(friend.getID(), walkTime);
        }
    }


    private void updateCurrentLocation() {
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences("location", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("changed", true)) {
            double lati = sharedPreferences.getFloat("latitude", (float) currentLocation.getLatitude());
            double longi = sharedPreferences.getFloat("longitude", (float) currentLocation.getLongitude());
            currentLocation.setLongitude(longi);
            currentLocation.setLatitude(lati);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("changed", false);
            edit.apply();
        }
        Log.d(LOG_TAG, "Current Location Updated to: " + currentLocation.toString());
    }

}
