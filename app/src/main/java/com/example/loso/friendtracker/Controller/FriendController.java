package com.example.loso.friendtracker.Controller;

import android.content.Intent;
import android.util.Log;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Model;
import com.example.loso.friendtracker.Service.ContactDataManager;
import com.example.loso.friendtracker.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Lettisia George on 31/08/2017.
 */

public class FriendController {
    private static final String LOG_TAG = "friendcontroller";
    private Model mModel = Model.getInstance();

    public FriendController() {
        if (mModel == null) {
            mModel = Model.getInstance();
        }
    }

    public String getFriendName(String friendID) {
        Friend friend = mModel.findFriendByID(friendID);
        return friend.getName();
    }

    public String getFriendEmail(String friendID) {
        Friend friend = mModel.findFriendByID(friendID);
        return friend.getEmail();
    }


    public void setBirthday(String friendID, int year, int month, int day) {
        Friend friend = mModel.findFriendByID(friendID);
        if (friend != null) {
            friend.setBirthday(new GregorianCalendar(year, month, day).getTime());
        }
    }

    public void updateFriendDetails(String friendID, String name, String email) {
        mModel.updateFriend(friendID, name, email);
    }

    public void addFriendFromContacts(MainActivity mainActivity, Intent data) {
        ContactDataManager contactsManager = new ContactDataManager(mainActivity, data);
        String name = "";
        String email = "";
        try {
            name = contactsManager.getContactName();
            email = contactsManager.getContactEmail();
            mModel.addFriend(name, email);
            //Log.d(LOG_TAG, "Added Friend");
        } catch (ContactDataManager.ContactQueryException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public String getFriendBirthday(String friendID) {
        Friend friend = mModel.findFriendByID(friendID);
        Date birthday = friend.getBirthday();
        if (birthday != null) {
            DateFormat mFormat = SimpleDateFormat.getDateInstance();
            return mFormat.format(birthday);
        }
        return " ";
    }

    public void removeFriend(String friendID) {
        mModel.removeFriend(friendID);
    }
}
