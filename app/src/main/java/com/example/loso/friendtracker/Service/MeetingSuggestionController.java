package com.example.loso.friendtracker.Service;

import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.FriendComparator;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.MeetingComparator;
import com.example.loso.friendtracker.Model.WalkTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Loso on 2017/9/28.
 */

public class MeetingSuggestionController {
    public static final int INITIAL = 0;
    public static final int NO = 1;
    public static final int YES = 2;
    private static final long DEFAULT_MEETING_DURATION = 60 * 60 * 1000; // 60 mins
    private static final String LOG_TAG = "MeetingSuggestion";
    private static MeetingSuggestionController instance;
    private Meeting suggestion;
    private int index;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Meeting createASuggestedMeeting(Location currentLocation)
    {
        if(status == NO)
            index++;

        //get friend list
        FriendController friendController = new FriendController();
        ArrayList<Friend> friends = friendController.getFriendsList();
        FriendComparator comp = new FriendComparator();
        Collections.sort(friends, comp);
        if(index >= friends.size())
            index = 0;
        Friend near = friends.get(index);

        //create a suggestion meeting
        MeetingController meetingController = new MeetingController();
        final Meeting suggest = meetingController.createTempMeeting();

        Location midPoint = null;
        if (near != null && near.getLocation() != null && currentLocation != null) {
            midPoint = currentLocation.getMidPoint(near.getLocation());
        } else {
            return suggest;
        }

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
                suggest.getStartDate().setTime(start);
                suggest.getEndDate().setTime(end);

                suggestion = suggest;
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, midPoint, near.getLocation(), currentLocation);
        meetingLocationTask.execute();

        return suggestion;
    }
}
