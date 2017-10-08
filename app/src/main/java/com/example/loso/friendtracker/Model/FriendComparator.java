package com.example.loso.friendtracker.Model;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Loso on 2017/9/6.
 */

public class FriendComparator implements Comparator<Friend> {

    @Override
    public int compare(Friend o1, Friend o2) {
        double walk1 = o1.getWalkTime().getNumericTime();
        double walk2 = o2.getWalkTime().getNumericTime();
        if (walk1 > walk2)
            return -1;
        else if (walk1 < walk2)
            return 1;
        else
            return 0;
    }
}
