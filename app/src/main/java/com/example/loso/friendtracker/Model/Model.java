package com.example.loso.friendtracker.Model;

import android.util.Log;

import java.util.Date;
import java.util.ArrayList;
import java.util.Observable;
import java.util.UUID;


/**
 * Model class. Singleton pattern.
 * <p>
 * Contains static helper method createID() to generate unique IDs for either meetings or classes
 *
 * @author Lettisia George
 */

public final class Model extends Observable {
    private static Model instance = null;

    public static final int FRIENDS_CHANGED = 24;
    public static final int MEETINGS_CHANGED = 67;

    private ArrayList<Friend> friends;
    private ArrayList<Meeting> meetings;

    private Model() {
        // Do not instantiate me please
        friends = new ArrayList<>();
        meetings = new ArrayList<>();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public static String createID() {
        return UUID.randomUUID().toString();
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
        setChanged();
        notifyObservers(FRIENDS_CHANGED);
    }

    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
        setChanged();
        notifyObservers(MEETINGS_CHANGED);
    }

    public boolean removeFriend(Friend friend) {
        boolean done = friends.remove(friend);
        if (done) {
            setChanged();
            notifyObservers(FRIENDS_CHANGED);
        }
        return done;
    }

    public boolean removeMeeting(Meeting meet) {
        boolean done = meetings.remove(meet);
        if (done) {
            setChanged();
            notifyObservers(MEETINGS_CHANGED);
        }
        return done;
    }

    public boolean removeFriend(String ID) {
        for (Friend f : friends) {
            if (f.getID().equals(ID)) {
                boolean done = friends.remove(f);
                if (done) {
                    Log.d("Model", "in remove friend");
                    setChanged();
                    notifyObservers(FRIENDS_CHANGED);
                }
                return done;
            }
        }
        return false;
    }

    public boolean removeMeeting(String ID) {
        for (Meeting m : meetings) {
            if (m.getID().equals(ID)) {
                boolean done = meetings.remove(m);
                if (done) {
                    setChanged();
                    notifyObservers(MEETINGS_CHANGED);
                }
                return done;
            }
        }
        return false;
    }

    public void addFriend(String name, String email) {
        friends.add(new Friend(createID(), name, email));
        setChanged();
        notifyObservers(FRIENDS_CHANGED);
    }

    public void addFriend(String name, String email, Date birthday) {

        friends.add(new Friend(createID(), name, email, birthday));
        setChanged();
        notifyObservers(FRIENDS_CHANGED);
    }

    public void addMeeting(String title, String start, String time, FriendLocation location) {
        meetings.add(new Meeting(createID(), title, start, time, friends, location));
        setChanged();
        notifyObservers(MEETINGS_CHANGED);

    }

    public Friend findFriendByID(String ID) {
        Friend found = null;
        for (Friend f : friends) {
            if (f.getID().equals(ID)) {
                found = f;
            }
        }
        return found;
    }

    public Meeting findMeetingByID(String ID) {
        Meeting found = null;
        for (Meeting m : meetings) {
            if (m.getID().equals(ID)) {
                found = m;
            }
        }
        return found;
    }

    /**
     * @param name
     * @return list of friends with provided name.
     */
    public ArrayList<Friend> findFriendByName(String name) {
        ArrayList<Friend> found = new ArrayList<>();
        for (Friend f : friends) {
            if (f.getName().equals(name)) {
                found.add(f);
            }
        }
        return found;
    }

    public boolean isFriend(String name, String email) {
        for (Friend f : friends) {
            if (f.getName().equals(name) && f.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
        setChanged();
        notifyObservers(FRIENDS_CHANGED);
    }

    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
        setChanged();
        notifyObservers(MEETINGS_CHANGED);
    }


    public void updateFriend(String friendID, String name, String email) {
        Friend friend = findFriendByID(friendID);
        if (friend != null) {
            friend.setName(name);
            friend.setEmail(email);
            setChanged();
            notifyObservers(FRIENDS_CHANGED);
        }
    }
}
