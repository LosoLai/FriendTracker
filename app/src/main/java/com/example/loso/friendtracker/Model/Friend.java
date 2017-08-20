package com.example.loso.friendtracker.Model;

/**
 * Created by Lettisia on 2017/8/19.
 */
import java.util.Date;

public class Friend {
    private String ID;
    private String name;
    private String phone;
    private String email;
    private Date birthday;
    private String photo;
    private Location location;

    public Friend(String iD, String name, String phone,String email, Date birthday) {
        ID = iD;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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