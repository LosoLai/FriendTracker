package com.example.loso.friendtracker.Model;

//import java.time.LocalDateTime;

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
	//private Date startDate;
	//private Date startTime;
	private String startDate;
	private String startTime;
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

	public Meeting(String iD, String title, String date, String time, List<Friend> friends,
				   FriendLocation location) {
		ID = iD;
		this.title = title;
		this.startDate = date;
		this.startTime = time;
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

	public String getStartDate() {
		return startDate;
	}

	public void setDate(String start) {
		this.startDate = start;
	}

	public String getTime() {
		return startTime;
	}

	public void setTime(String time) {
		this.startTime = time;
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
}
