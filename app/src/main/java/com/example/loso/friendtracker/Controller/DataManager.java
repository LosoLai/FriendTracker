package com.example.loso.friendtracker.Controller;

/**
 * Created by Loso on 2017/8/19.
 * Repurposed by Lettisia George on 01/09/2017
 */
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataManager {
    //private ArrayList<Meeting> meetings;
    // private ArrayList<Friend> friends;

    public static ArrayList<Friend> createDummyFriendList() {
        ArrayList<Friend> friends = new ArrayList<Friend>();
        for(int i=1 ; i<10 ; i++) {
            String id = Model.createID();
            String name = "Friend" + i;
            //        String phone = "Phone" + i; // no requirement in assignmetn for phone number
            String email = "Email" + i;
            Date birthday = new GregorianCalendar(1985 + i, (i + 5) % 12, i % 28).getTime();
            friends.add(new Friend(id, name, email, birthday));
        }
        return friends;
    }

    public static ArrayList<Meeting> createDummMeetingList() {
        ArrayList<Meeting> meetings = new ArrayList<Meeting>();
        ;
        for (int i = 0; i < 3; i++) {
            meetings.add(new Meeting(Model.createID(), "Meeting" + Integer.toString(i)));
        }
        return meetings;
    }
}