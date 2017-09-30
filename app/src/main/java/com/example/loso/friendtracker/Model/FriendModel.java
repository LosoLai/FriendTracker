package com.example.loso.friendtracker.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.UUID;

/**
 * FriendModel class. Singleton pattern.
 * <p>
 * Contains static helper method createID() to generate unique IDs for either meetings or classes
 *
 * @author Lettisia George
 */

public class FriendModel extends Observable {
    private static FriendModel instance = null;
    private ArrayList<Friend> friends;


    private FriendModel() {
        friends = new ArrayList<>();
    }

    public static FriendModel getInstance() {
        if (instance == null) {
            instance = new FriendModel();
        }
        return instance;
    }

    public static String createID() {
        return UUID.randomUUID().toString();
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
        setChanged();
        notifyObservers();
    }

    public boolean removeFriend(Friend friend) {
        boolean done = friends.remove(friend);
        if (done) {
            setChanged();
            notifyObservers();
        }
        return done;
    }

    public boolean removeFriend(String ID) {
        for (Friend f : friends) {
            if (f.getID().equals(ID)) {
                boolean done = friends.remove(f);
                if (done) {
                    Log.d("MeetingModel", "in remove friend");
                    setChanged();
                    notifyObservers();
                }
                return done;
            }
        }
        return false;
    }

    public void addFriend(String name, String email) {
        friends.add(new Friend(createID(), name, email));
        setChanged();
        notifyObservers();
    }

    public void addFriend(String name, String email, Date birthday) {

        friends.add(new Friend(createID(), name, email, birthday));
        setChanged();
        notifyObservers();
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
        notifyObservers();
    }

    public void updateFriend(String friendID, String name, String email) {
        Friend friend = findFriendByID(friendID);
        if (friend != null) {
            friend.setName(name);
            friend.setEmail(email);
            setChanged();
            notifyObservers();
        }
    }

    public void updateFriendLocation(String friendID, Location location) {
        Friend friend = findFriendByID(friendID);
        if (friend != null) {
            friend.setLocation(location);
            setChanged();
            notifyObservers(friend);
        }
    }

    public Location getFriendLocation(String friendID) {
        Friend friend = findFriendByID(friendID);
        if (friend != null) {
            return friend.getLocation();
        }
        return null;
    }

    public void updateFriendWalkTime(String friendID, double walkTime) {
        Friend friend = findFriendByID(friendID);
        if (friend != null) {
            friend.setWalkTime(walkTime);
        }
    }
}
