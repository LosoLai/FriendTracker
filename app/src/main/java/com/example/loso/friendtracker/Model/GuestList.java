package com.example.loso.friendtracker.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by Lettisia George on 16/09/2017.
 *
 * hashmap with friend as key and walk time as value.
 *
 * In service: go through all meeting guest lists to update walk times to meeting.
 */

public class GuestList extends Observable {
    private HashMap<Friend, Double> guests = new HashMap<>();

    public GuestList() {
    }

    public GuestList(ArrayList<Friend> friends) {
        for (Friend f : friends) {
            guests.put(f, 0.0);
        }

    }

    public void addFriend(Friend friend) {
        guests.put(friend, 0.0);
        setChanged();
        notifyObservers();
    }

    public void removeFriend(String friendID) {
        for (Friend friend : guests.keySet()) {
            if (friend.getID().equals(friendID)) {
                guests.remove(friend);
            }
        }
        setChanged();
        notifyObservers();
    }

    // only to be used for save by Database helpers
    public HashMap<Friend, Double> getGuestList() {
        return guests;
    }

    // only to be used for load by Database helpers
    public void setGuests(HashMap<Friend, Double> friends) {
        guests = friends;
        setChanged();
        notifyObservers();
    }

    public void addGuests(ArrayList<Friend> friends) {
        for (Friend f : friends) {
            guests.put(f, 0.0);
        }
        setChanged();
        notifyObservers();
    }

    public HashMap<Friend, Double> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<Friend> friends) {
        guests.clear();
        for (Friend f : friends) {
            guests.put(f, 0.0);
        }
        setChanged();
        notifyObservers();
    }
}
