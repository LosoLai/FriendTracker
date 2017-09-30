package com.example.loso.friendtracker.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.loso.friendtracker.Controller.AttendListAdapter;
import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayAttendList extends AppCompatActivity {
    final static int ATTEND_CHANGED = 128;
    private String meetingID = "";
    private MeetingController meetingController;
    private FriendController friendController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_attends);

        // This activity interacts with one specific meetig
        // Grab meetig ID as passed as an extra in the intent
        meetingID = getIntent().getStringExtra("meeting");
        meetingController = new MeetingController();
        friendController = new FriendController();
        HashMap<Friend, Double> meeting_Attend = meetingController.getMeetingAttendees(meetingID);
        ArrayList<Friend> friendslist = friendController.getFriendsList();

        ArrayList<Friend> filtered = new ArrayList<Friend>();
        // filter friends
        for (int i = 0; i < friendslist.size(); i++) {
            boolean bSame = false;
            Friend friend = friendslist.get(i);
            for (Friend attend : meeting_Attend.keySet()) {
                if (friend == attend) {
                    bSame = true;
                    break;
                }
            }

            if (!bSame)
                filtered.add(friend);
        }

        ListView friendsList = (ListView) findViewById(R.id.lvFriendsList);
        AttendListAdapter adapter = new AttendListAdapter(getApplicationContext(), filtered);
        friendsList.setAdapter(adapter);
    }
}
