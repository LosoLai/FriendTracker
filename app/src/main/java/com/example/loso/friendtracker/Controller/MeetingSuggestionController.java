package com.example.loso.friendtracker.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.GuestList;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.WalkTime;
import com.example.loso.friendtracker.Service.MeetingLocationTask;
import com.example.loso.friendtracker.Service.WalkigTimeCallBack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Loso on 2017/9/28.
 */

public class MeetingSuggestionController {
    public static final long DEFAULT_MEETING_DURATION = 60 * 60 * 1000; // 60 mins
    private static final String LOG_TAG = "MeetingSuggestion";
    private Context context;
    private Location midPoint;
    private GuestList guestList;
    private double maxWalkTime;

    public MeetingSuggestionController(Context context) {
        this.context = context;
    }

    public Meeting createASuggestedMeeting(Location currentLocation)
    {
        MeetingController meetingController = new MeetingController();
        final Meeting suggest = meetingController.createTempMeeting();

        //get friend list
        FriendController friendController = new FriendController();
        ArrayList<Friend> friends = friendController.getFriendsList();
        Friend near = null;
        for(int i=0 ; i<friends.size() ; i++)
        {
            Friend current = friends.get(i);
            if(near == null)
                near = current;

            if(current.getLocation() == null || current.getWalkTime().getNumericTime() < 0)
                continue;

            if(current.getWalkTime().getNumericTime() < near.getWalkTime().getNumericTime())
                near = current;
        }

        //get mid location
        Location midPoint = null;
        if(near != null && near.getLocation() != null)
            midPoint = currentLocation.getMidPoint(near.getLocation());

        //create a suggestion meeting
        suggest.setTitle("Suggestion_" + near.getName());
        suggest.setLocation(midPoint);
        suggest.addAttend(near);
        Calendar current = Calendar.getInstance();
        final Date startDate = new Date(current.getTimeInMillis());
        final Date endDate = new Date(current.getTimeInMillis());
        suggest.setStartDate(startDate);
        suggest.setEndDate(endDate);

        //get two locations walkingTime
        MeetingLocationTask meetingLocationTask = new MeetingLocationTask(new WalkigTimeCallBack<WalkTime>() {
            @Override
            public void onSuccess(WalkTime object) {
                double maxWalk = object.getNumericTime();
                long start = suggest.getStartDate().getTime() + (long)(maxWalk * 1000);
                long end = start + DEFAULT_MEETING_DURATION;
                suggest.setStartDate(new Date(start));
                suggest.setEndDate(new Date(end));
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, midPoint, near.getLocation(), currentLocation);
        meetingLocationTask.execute();


        return suggest;
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
                maxWalkTime = bundle.getDouble(MeetingLocationTask.MAX_WALKTIME);
                Log.d(LOG_TAG, "Max walk time: " + maxWalkTime);
            }
        }, intentFilter);
    }

    public WalkTime getMaxWalkingTime(Location[] locations)
    {
        WalkTime maxWalkingTime = new WalkTime();
        return maxWalkingTime;
    }
}
