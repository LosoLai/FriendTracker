package com.example.loso.friendtracker.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.UUID;


/**
 * MeetingModel class. Singleton pattern.
 * <p>
 * Contains static helper method createID() to generate unique IDs for either meetings or classes
 *
 * @author Lettisia George
 */

public final class MeetingModel extends Observable {
    private static MeetingModel instance = null;
    private ArrayList<Meeting> meetings;

    private MeetingModel() {
        // Do not instantiate me please
        meetings = new ArrayList<>();
    }

    public static MeetingModel getInstance() {
        if (instance == null) {
            instance = new MeetingModel();
        }
        return instance;
    }

    public static String createID() {
        return UUID.randomUUID().toString();
    }

    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
        setChanged();
        notifyObservers();
    }

    public boolean removeMeeting(String meet) {
        boolean done = meetings.remove(meet);
        if (done) {
            setChanged();
            notifyObservers();
        }
        return done;
    }

    public Meeting findMeetingByID(String ID) {
        Meeting found = null;
        for (Meeting m : meetings) {
            if (m.getID().equals(ID)) {
                found = m;
            }
        }
        return found;
    }


    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
        setChanged();
        notifyObservers();
    }

    public void updateMeeting(String meetingID, String title, double lati, double longi) {
        Meeting meet = findMeetingByID(meetingID);
        if (meet != null) {
            meet.setTitle(title);
            meet.setLocation(lati, longi);
            setChanged();
            notifyObservers();
        }
    }

    public ArrayList<Friend> getMeetingAttendees(String meetingID) {
        Meeting meet = findMeetingByID(meetingID);
        if (meet != null) {
            return meet.getFriends();
        } else {
            return null;
        }
    }

    public void setMeetingTimes(String meetingID, Date newStart, Date newEnd) throws InvalidDateException {
        Meeting meeting = findMeetingByID(meetingID);
        if (meeting != null) {
            Date now = Calendar.getInstance().getTime();
            if (newStart.before(now)) {
                throw new InvalidDateException("Start is before current time");
            } else if (newEnd.before(now)) {
                throw new InvalidDateException("End is before current time");
            } else if (newEnd.before(newStart)) {
                throw new InvalidDateException("End is before Start");
            } else {
                meeting.setStartDate(newStart);
                meeting.setEndDate(newEnd);
                setChanged();
                notifyObservers();
            }
        }
    }

    public Date[] getMeetingTimes(String meetingID) {
        Meeting meet = findMeetingByID(meetingID);
        Date[] dates = new Date[2];
        if (meet != null) {
            dates[0] = meet.getStartDate();
            dates[1] = meet.getEndDate();
        }
        return dates;
    }

    public void removeGuest(String meetingID, String friendID) {
        Meeting meet = findMeetingByID(meetingID);
        if (meet != null) {
            meet.removeAttend(friendID);
        }
    }

    public class InvalidDateException extends Throwable {
        public InvalidDateException(String s) {
            super(s);
        }
    }
}
