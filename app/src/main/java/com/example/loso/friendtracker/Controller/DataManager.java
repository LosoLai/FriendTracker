package com.example.loso.friendtracker.Controller;

/**
 * Created by Loso on 2017/8/19.
 */
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Meeting;

import java.util.Date;
import java.util.HashMap;

public class DataManager {
    private static DataManager instance = null;
    private HashMap<String, Friend> friends;
    private HashMap<String, Meeting> meetings;

    protected DataManager() {
        friends = new HashMap<String, Friend>();
        meetings = new HashMap<String, Meeting>();
        // create dummy for testing
        createDummyFriendList();
    }

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void createDummyFriendList() {
        for(int i=1 ; i<10 ; i++) {
            String id = "ID000" + i;
            String name = "Friend" + i;
            String email = "Email" + i;
            Date birthday = null;
            friends.put(id, new Friend(id, name, email, birthday));
        }
    }

    public DataManager(HashMap<String, Friend> friends, HashMap<String, Meeting> meetings) {
        super();
        this.friends = friends;
        this.meetings = meetings;
    }

}