package com.example.loso.friendtracker.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contains meeting data
 *
 * @author Lettisia George
 */

public class Meeting {
	private String ID;
	private String title;
    private Date startDate;
    private Date endDate;
    private List<Friend> friends;
	private FriendLocation location;

	public Meeting() {
		friends = new ArrayList<Friend>();
		ID = "user" + Long.toString(System.currentTimeMillis());
	}

	public Meeting(String id) {
		friends = new ArrayList<Friend>();
		ID = id;
	}

    public Meeting(String iD, String title) {
        ID = iD;
        this.title = title;
    }

    public Meeting(String iD, String title, List<Friend> friends,
                   FriendLocation location) {
		ID = iD;
		this.title = title;
        this.startDate = null;
        this.endDate = null;
        this.friends = friends;
		this.location = location;
	}

    public Meeting(String iD, String title, Date start, Date end, List<Friend> friends,
                   FriendLocation location) {
        ID = iD;
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.friends = friends;
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

    public Date getTime() {
        return endDate;
    }

    public void setEndDate(Date end) {
        this.endDate = end;
    }

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	public FriendLocation getLocation() {
		return location;
	}

	public void setLocation(FriendLocation location) {
		this.location = location;
	}

    public void setLocation(double lati, double longi) {
        location = new FriendLocation(lati, longi);
    }
}
