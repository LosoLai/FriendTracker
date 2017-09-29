package com.example.loso.friendtracker.Model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Lettisia George on 16/09/2017.
 *
 * hashmap with friend as key and walk time as value.
 *
 * In service: go through all meeting guest lists to update walk times.
 */

public class GuestList extends Observable {
    private ArrayList<Friend> guests = new ArrayList<>();

    public GuestList() {
    }

    public GuestList(ArrayList<Friend> friends) {
        guests.addAll(friends);
    }

    public void addFriend(Friend friend) {
        guests.add(friend);
        setChanged();
        notifyObservers();
    }

    public void removeFriend(String friendID) {
        for (Friend friend : guests) {
            if (friend.getID().equals(friendID)) {
                guests.remove(friend);
            }
        }
        setChanged();
        notifyObservers();
    }

    // only to be used for save by Database helpers
    public ArrayList<Friend> getGuestList() {
        return guests;
    }

    // only to be used for load by Database helpers
    public void setGuests(ArrayList<Friend> friends) {
        guests = friends;
        setChanged();
        notifyObservers();
    }

    public void addGuests(ArrayList<Friend> friends) {
        guests.addAll(friends);
    }
}
