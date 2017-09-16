package com.example.loso.friendtracker.Controller;


import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.MeetingModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by Lettisia George on 31/08/2017.
 */

public class MeetingController {
    private static final String LOG_TAG = "meetingcontroller";
    private MeetingModel mMeetingModel = MeetingModel.getInstance();

    public MeetingController() {
        if (mMeetingModel == null) {
            mMeetingModel = MeetingModel.getInstance();
        }
    }


    public void updateMeetingDetails(String meetingID, String title, double lati, double longi) {
        mMeetingModel.updateMeeting(meetingID, title, lati, longi);
    }

    public void setMeetingTimes(String meetingID, Date newStart, Date newEnd) throws MeetingModel.InvalidDateException {
        mMeetingModel.setMeetingTimes(meetingID, newStart, newEnd);
    }

    public void removeMeeting(String meeting) {
        mMeetingModel.removeMeeting(meeting);
    }

    public void removeAttend(String meeting, String friend) {
        mMeetingModel.removeGuest(meeting, friend);
    }

    public Location getMeetingLocation(String meetingID) {
        return mMeetingModel.findMeetingByID(meetingID).getLocation();
    }

    public ArrayList<Friend> getMeetingAttendees(String meetingID) {
        return mMeetingModel.getMeetingAttendees(meetingID);
    }

    public Meeting createNewMeeting() {
        Meeting meet = new Meeting(MeetingModel.createID());
        mMeetingModel.addMeeting(meet);
        return meet;
    }

    public String getMeetingTitle(String meetingID) {
        return mMeetingModel.findMeetingByID(meetingID).getTitle();
    }


    public Date[] getMeetingTimes(String meetingID) {
        return mMeetingModel.getMeetingTimes(meetingID);
    }
}
