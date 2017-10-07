package com.example.loso.friendtracker.Service;

import android.content.Context;

import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.GuestList;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.WalkTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Loso on 2017/9/28.
 */

public class MeetingSuggestionController {
    public static final long DEFAULT_MEETING_DURATION = 60 * 60 * 1000; // 60 mins
    private static final String LOG_TAG = "MeetingSuggestion";
    private static MeetingSuggestionController instance;
    private Meeting suggestion;

    public static MeetingSuggestionController getInstance()
    {
        if(instance == null)
            instance = new MeetingSuggestionController();

        return instance;
    }
    public Meeting getSuggestion()
    {
        return suggestion;
    }

    public Meeting createASuggestedMeeting(Location currentLocation)
    {
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
        MeetingController meetingController = new MeetingController();
        final Meeting suggest = meetingController.createTempMeeting();
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

        suggestion = suggest;
        return suggestion;
    }
}
