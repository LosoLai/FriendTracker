package com.example.loso.friendtracker.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.FriendLocation;
import com.example.loso.friendtracker.Model.Model;
import com.example.loso.friendtracker.Service.ContactDataManager;
import com.example.loso.friendtracker.Service.DataManager;
import com.example.loso.friendtracker.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public void updateFriendLocations(Context context) {
        DataManager dm = new DataManager();
        ArrayList<Friend> friends = mModel.getFriends();
        if (friends != null) {
            for (Friend f : friends) {
                FriendLocation fl = DataManager.getFriendLocation(context, f.getName(), Calendar.getInstance().getTime());
                if (fl != null) {
                    f.setLocation(fl);
                    Log.d(LOG_TAG, "Time: " + Calendar.getInstance().getTime() + " location: " + fl.toString());
                }
            }
        }
    }

    public static FriendLocation getFriendLocationsForTime(Context context, String name) {
        FriendLocation fl = DataManager.getFriendLocation(context, name, Calendar.getInstance().getTime());
        if (fl == null) {
            Log.d(LOG_TAG, "null");
        } else {
            Log.d(LOG_TAG, "Time: " + Calendar.getInstance().getTime() + " location: " + fl.toString());
        }
        return fl;
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

    public boolean addFriendFromContacts(MainActivity mainActivity, Intent data) {
        ContactDataManager contactsManager = new ContactDataManager(mainActivity, data);
        String name = "";
        String email = "";
        try {
            name = contactsManager.getContactName();
            email = contactsManager.getContactEmail();
            if (!mModel.isFriend(name, email)) {
                mModel.addFriend(name, email);
                return true;
            }
            //Log.d(LOG_TAG, "Added Friend");
        } catch (ContactDataManager.ContactQueryException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return false;
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

    public void removeFriend(Friend friend) {
        mModel.removeFriend(friend);
    }


}
