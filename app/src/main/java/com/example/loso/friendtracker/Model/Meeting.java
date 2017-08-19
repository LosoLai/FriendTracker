package com.example.loso.friendtracker.Model;

/**
 * Created by Lettisia on 2017/8/19.
 */
// comment by Loso
//import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;


public class Meeting {
    private String ID;
    private String title;
    private Date start;
    private Date end;
    private LinkedList<Friend> friends;
    private Location location;

    public Meeting() {
        friends = new LinkedList<Friend>();
        ID = "user" + Long.toString(System.currentTimeMillis());
    }

    public Meeting(String id) {
        friends = new LinkedList<Friend>();
        ID = id;
    }

    public Meeting(String iD, String title, Date start, Date end, LinkedList<Friend> friends,
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

    public LinkedList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(LinkedList<Friend> friends) {
        this.friends = friends;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
