package com.example.loso.friendtracker.Controller;


import com.example.loso.friendtracker.Model.FriendLocation;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.Model;


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

    public void updateMeetingDetails(String meetingID, String title) {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            //meeting.setLocation(location);
            meeting.setTitle(title);
        }
    }

    public void setMeetingDate(String meetingID, int year, int month, int day) {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            String date = day + "/" + month + "/" + year;
            meeting.setDate(date);
        }
    }

    public void setMeetingTime(String meetingID, int hourOfDay, int minute) {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            String time = hourOfDay + ":" + minute;
            meeting.setTime(time);
        }
    }

    public void removeMeeting(Meeting meeting) {
        mModel.removeMeeting(meeting);
    }
}
