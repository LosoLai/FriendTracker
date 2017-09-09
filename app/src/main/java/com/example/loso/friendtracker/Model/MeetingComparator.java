package com.example.loso.friendtracker.Model;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Loso on 2017/9/6.
 */

public class MeetingComparator implements Comparator<Meeting> {
    public final static int ORDER_ACS = 0;
    public final static int ORDER_DECS =1;
    private int orderType;

    public MeetingComparator(int orderType)
    {
        this.orderType = orderType;
    }

    @Override
    public int compare(Meeting o1, Meeting o2) {
        Date date1 = o1.getStartDate();
        Date date2 = o2.getStartDate();
        if(date1 != null && date2 != null) {
            if(orderType == ORDER_ACS)
                return date1.compareTo(date2);
            if(orderType == ORDER_DECS) {
                if (date1.compareTo(date2) > 0)
                    return -1;
                else if (date1.compareTo(date2) < 0)
                    return 1;
                else
                    return 0;
            }
        }
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
