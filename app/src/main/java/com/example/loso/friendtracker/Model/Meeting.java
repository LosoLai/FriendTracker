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
	private Date start;
	private Date end;
	private List<Friend> friends;
	private Location location;

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

	public Meeting(String iD, String title, Date start, Date end, List<Friend> friends,
				   Location location) {
		ID = iD;
		this.title = title;
		this.start = start;
		this.end = end;
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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
