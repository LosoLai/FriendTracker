package com.example.loso.friendtracker.Controller;

/**
 * Created by Loso on 2017/8/19.
 */
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Meeting;

import java.util.ArrayList;
import java.util.Date;

public class DataManager {
    private static DataManager instance = null;
    private ArrayList<Friend> friends;
    private ArrayList<Meeting> meetings;

    protected DataManager() {
        friends = new ArrayList<Friend>();
        meetings = new ArrayList<Meeting>();
        // create dummy for testing
        createDummyFriendList();
    }

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<Friend> getFriendList() {return friends;}

    public ArrayList<Meeting> getMeetingList() {return meetings;}

    private void createDummyFriendList() {
        for(int i=1 ; i<10 ; i++) {
            String id = "ID000" + i;
            String name = "Friend" + i;
            //        String phone = "Phone" + i; // no requirement in assignmetn for phone number
            String email = "Email" + i;
            Date birthday = null;
            friends.add(new Friend(id, name, email, birthday));
        }
    }

    public DataManager(ArrayList<Friend> friends, ArrayList<Meeting> meetings) {
        super();
        this.friends = friends;
        this.meetings = meetings;
    }
}