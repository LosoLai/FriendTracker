package com.example.loso.friendtracker.Service;

/**
 * Created by Loso on 2017/8/19.
 * Repurposed by Lettisia George on 01/09/2017
 */
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.FriendLocation;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DataManager {
    private static final String LOG_TAG = DummyLocationService.class.getName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList<Friend> createDummyFriendList(Context context) {
        //scan location first
        DummyLocationService dummyLocationService = DummyLocationService.getSingletonInstance(context);
        //dummyLocationService.logAll();

        //create dummy friend list
        ArrayList<Friend> friends = new ArrayList<Friend>();
        for(int i=1 ; i<10 ; i++) {
            String id = Model.createID();
            String name = "Friend" + i;
            String email = "Email" + i;
            Date birthday = new GregorianCalendar(1985 + i, (i + 5) % 12, i % 28).getTime();

            //get dummy location
            FriendLocation location = null;
            try {
                //Log.d(LOG_TAG, Calendar.getInstance().getTime());
                location = getFriendLocation(context, name, Calendar.getInstance().getTime());
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            friends.add(new Friend(id, name, email, birthday, location));
        }
        return friends;
    }

    public static ArrayList<Meeting> createDummMeetingList() {
        ArrayList<Meeting> meetings = new ArrayList<Meeting>();
        ;
        for (int i = 0; i < 3; i++) {
            String title = "Meeting" + Integer.toString(i);
            int[] dates = {6,4,7};
            List<Friend> friends = new ArrayList<Friend>();
            friends.add(new Friend(Model.createID(), Integer.toString(i), Integer.toString(i), null, null));
            Meeting item = new Meeting(Model.createID(), "Meeting" + Integer.toString(i), friends, null);
            Calendar cal = Calendar.getInstance();
            cal.set(2017, 9, dates[i], 10, 30);
            Date date = cal.getTime();
            item.setStartDate(date);
            meetings.add(item);
        }
        return meetings;
    }

    public static FriendLocation getFriendLocation(Context context, String name, Date time) {
        List<DummyLocationService.FriendLocation> list = getFriendLocationsForTime(context, name, time, 2, 0);
        return grabFriendLocation(name, list);
    }

    public static FriendLocation grabFriendLocation(String name, List<DummyLocationService.FriendLocation> locs) {
        FriendLocation found = null;
        for (DummyLocationService.FriendLocation fl : locs) {
            //Log.d(LOG_TAG, fl.name + "=" + name);
            if (fl.name.equals(name)) {
                found = new FriendLocation(fl.time, fl.latitude, fl.longitude);
            }
        }
        return found;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<DummyLocationService.FriendLocation> getFriendLocationsForTime(Context context, String name, Date time, int periodMinutes, int periodSeconds) {
        DummyLocationService dummyLocationService = DummyLocationService.getSingletonInstance(context);
        List<DummyLocationService.FriendLocation> matched = null;
        // 2 mins either side of 9:46:30 AM
        try {
            matched = dummyLocationService.getFriendLocationsForTime(time, 2, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (matched.size() > 0) {
            //Log.i(LOG_TAG, "Matched Query:");
            dummyLocationService.log(matched);
        }
        return matched;
    }
}