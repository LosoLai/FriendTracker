package com.example.loso.friendtracker.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.GuestList;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.WalkTime;
import com.example.loso.friendtracker.Service.MeetingLocationTask;

import java.util.ArrayList;

/**
 * Created by Loso on 2017/9/28.
 */

public class MeetingSuggestionController {
    private Context context;
    private Location midPoint;
    private GuestList guestList;
    private double maxWalkTime;

    public MeetingSuggestionController(Context context) {
        this.context = context;
    }

    public Location getMeetingLocation(Location[] locations)
    {
        midPoint = Location.NEAR_RMIT;
        return midPoint;
    }

    public void getWalkingTime(Location startlocation, Location meetingLocation)
    {
        // some defaults for testing to be removed later
        // Definitely not controller code - should be in a service or view.
        FriendController fc = new FriendController();
        ArrayList<Friend> friends = fc.getFriendsList();
        guestList = new GuestList(friends);
        WalkTime walkingTime = new WalkTime();

        // Here's how to start a meeting async task
        MeetingLocationTask meetingLocationTask = new MeetingLocationTask(context, guestList, midPoint);
        meetingLocationTask.execute();

        // Here's how to get the data out once its finished
        IntentFilter intentFilter = new IntentFilter(MeetingLocationTask.MEETING_LOCATION_TASK);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                guestList = (GuestList) bundle.get(MeetingLocationTask.GUESTLIST);
                maxWalkTime = bundle.getDouble(MeetingLocationTask.MAX_WALKTIME);
            }
        }, intentFilter);
    }

    public WalkTime getMaxWalkingTime(Location[] locations)
    {
        WalkTime maxWalkingTime = new WalkTime();
        return maxWalkingTime;
    }
}
