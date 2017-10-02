package com.example.loso.friendtracker.Service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.GuestList;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.WalkTime;

/**
 * Created by Lettisia George on 01/10/2017.
 */

public class MeetingLocationTask extends AsyncTask<Void, Void, Void> {
    public static final String MEETING_LOCATION_TASK = "MeetingLocationTask";
    public static final String MAX_WALKTIME = "MaxWalk";
    public static final String GUESTLIST = "GuestList";
    private GuestList guests;
    private Location midPoint;
    private Context context;
    private double maxWalk;

    public MeetingLocationTask(Context context, GuestList guestList, Location midPoint) {
        this.context = context;
        guests = guestList;
        this.midPoint = midPoint;
    }


    @Override
    protected Void doInBackground(Void... unused) {
        maxWalk = 0.0;
        DistanceFinder distance = new DistanceFinder();
        for (Friend friend : guests.getGuestList().keySet()) {
            Location friendLocation = friend.getLocation();
            if (friendLocation != null) {
                String url = distance.generateDistanceURL(friendLocation, midPoint);
                String response = distance.accessURL(url);
                WalkTime walkTime = distance.parseWalkTime(response);
                guests.getGuestList().put(friend, walkTime);

                if (walkTime.getNumericTime() > maxWalk) {
                    maxWalk = walkTime.getNumericTime();
                }
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void unused) {
        Intent intent = new Intent(MEETING_LOCATION_TASK);
        intent.putExtra(MAX_WALKTIME, maxWalk);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


}
