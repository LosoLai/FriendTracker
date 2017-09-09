package com.example.loso.friendtracker.Controller;


import android.util.Log;

import com.example.loso.friendtracker.Model.FriendLocation;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;



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
            mModel.forceUpdate();
        }
    }

    public void updateMeetingDetails(String meetingID, String title, double lati, double longi) {
        mModel.updateMeeting(meetingID, title, lati, longi);
    }

    public void setMeetingStart(String meetingID, int year, int month, int day, int hour, int minute) throws InvalidDateException {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, minute);
            Date newStart = cal.getTime();
            Log.d(LOG_TAG, "newStart: " + newStart);
            Date now = Calendar.getInstance().getTime();
            Log.d(LOG_TAG, "now: " + now);
            if (newStart.before(now)) {
                throw new InvalidDateException("Start is before current time");
            } else {
                meeting.setStartDate(newStart);
                mModel.forceUpdate();
            }
        }
    }

    public void setMeetingEnd(String meetingID, int year, int month, int day, int hour, int minute) throws InvalidDateException {
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, minute);
            Date newEnd = cal.getTime();
            Date now = Calendar.getInstance().getTime();
            Date startDate = meeting.getStartDate();
            if (newEnd.before(now)) {
                throw new InvalidDateException("End is before current time");
            } else if (newEnd.before(startDate)) {
                throw new InvalidDateException("End is before Start");
            } else {
                meeting.setEndDate(newEnd);
                mModel.forceUpdate();
            }
        }
    }


    public void removeMeeting(Meeting meeting) {
        mModel.removeMeeting(meeting);
    }

    public void removeAttend(Meeting meeting, Friend friend) { meeting.removeAttend(friend); }

    public FriendLocation getMeetingLocation(String meetingID) {
        FriendLocation loc = null;
        Meeting meeting = mModel.findMeetingByID(meetingID);
        if (meeting != null) {
            loc = meeting.getLocation();
        }
        return loc;
    }

    public String[] getStartEndStrings(String meetingID) {
        String[] dates = new String[4];
        Meeting meeting = mModel.findMeetingByID(meetingID);
        Date curDate = Calendar.getInstance().getTime();
        if (meeting != null) {
            SimpleDateFormat sdf_Date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf_Time = new SimpleDateFormat("h:mm a");

            dates[0] = sdf_Date.format(curDate);
            dates[1] = sdf_Time.format(curDate);
            dates[2] = sdf_Date.format(curDate);
            dates[3] = sdf_Time.format(curDate);
        }
        return dates;
    }

    public Meeting createNewMeeting() {
        Meeting meet = new Meeting(Model.createID());
        mModel.addMeeting(meet);
        return meet;
    }



    public class InvalidDateException extends Throwable {
        public InvalidDateException(String s) {
            super(s);
        }
    }
}
