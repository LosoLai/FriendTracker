package com.example.loso.friendtracker.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Contains meeting data
 *
 * @author Lettisia George
 */

public class Meeting {
    private String ID;
    private String title = null;
    private Date startDate = null;
    private Date endDate = null;
    private GuestList friends = new GuestList();
    private Location location = null;

    private Meeting() {
        // Can't have a meeting without an ID
    }

    public Meeting(String id) {
        ID = id;
    }

    public Meeting(String iD, String title) {
        ID = iD;
        this.title = title;
    }

    public Meeting(String iD, String title, Date startDate, Date endDate, Location location) {
        ID = iD;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }

    public Meeting(String iD, String title, ArrayList<Friend> friends,
                   Location location) {
        ID = iD;
        this.title = title;
        this.friends.addGuests(friends);
        this.location = location;
    }

    public Meeting(String iD, String title, Date start, Date end, ArrayList<Friend> friends,
                   Location location) {
        ID = iD;
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.friends.addGuests(friends);
        this.location = location;
    }


    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date start) {
        this.startDate = start;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date end) {
        this.endDate = end;
    }

    public ArrayList<Friend> getFriends() {
        return friends.getGuestList();
    }

    public void setFriends(ArrayList<Friend> friends) {
        if (friends == null) {
            this.friends = new GuestList(friends);
        } else {
            this.friends.setGuests(friends);
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(double lati, double longi) {
        location = new Location(lati, longi);
    }

    public void removeAttend(String friendID) {
        if (friends != null) {
            friends.removeFriend(friendID);
        }
    }

	public void addAttend(Friend friend) {
		if(this.friends != null)
            friends.addFriend(friend);
	}
}
