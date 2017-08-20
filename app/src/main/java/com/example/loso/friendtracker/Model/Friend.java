package com.example.loso.friendtracker.Model;

import java.util.Date;

/**
 * Contains contact information for a friend
 *
 * @author Lettisia George
 */

public class Friend {
    private String ID;
    private String name;
    private String email;
    private Date birthday;
    private String photo;
    private Location location;

    public Friend(String iD, String name, String email, Date birthday) {
        ID = iD;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.photo = null;
        this.location = null;
    }

    public Friend(String iD, String name, String email) {
        ID = iD;
        this.name = name;
        this.email = email;
        this.birthday = null;
        this.photo = null;
        this.location = null;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
