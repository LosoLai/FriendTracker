package com.example.loso.friendtracker.Controller;


import com.example.loso.friendtracker.Model.FriendLocation;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.Model;

import java.util.GregorianCalendar;


/**
 * Created by Lettisia George on 31/08/2017.
 */

public class MeetingController {
    private static final String LOG_TAG = "meetingcontroller";
    private Model mModel = Model.getInstance();

    public MeetingController() {
        if (mModel == null) {
            mModel = Model.getInstance();
        }
    }

    public Meeting getMeeting(String meetingID) {
        return mModel.findMeetingByID(meetingID);
    }

    public void setLocation(String meetingID, FriendLocation location) {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            meeting.setLocation(location);
        }
    }

    public void updateMeetingDetails(String meetingID, String title, double lati, double longi) {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            meeting.setLocation(lati, longi);
            meeting.setTitle(title);
        }
    }

    public void setMeetingStart(String meetingID, int year, int month, int day, int hour, int minute) {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            meeting.setStartDate(new GregorianCalendar(year, month, day, hour, minute).getTime());
        }
    }

    public void setMeetingEnd(String meetingID, int year, int month, int day, int hour, int minute) {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            meeting.setEndDate(new GregorianCalendar(year, month, day, hour, minute).getTime());
        }
    }


    public void removeMeeting(Meeting meeting) {
        mModel.removeMeeting(meeting);
    }
}
