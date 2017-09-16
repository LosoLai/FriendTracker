package com.example.loso.friendtracker.Model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Lettisia George on 16/09/2017.
 */

public class GuestList extends Observable {
    private ArrayList<Friend> guests;

    public GuestList(ArrayList<Friend> friends) {
        guests = friends;
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
}
