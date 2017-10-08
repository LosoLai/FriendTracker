package com.example.loso.friendtracker.Controller;

import android.content.Context;

import com.example.loso.friendtracker.Database_SQLite.DatabaseHelper;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.FriendModel;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.MeetingModel;
import com.example.loso.friendtracker.Service.DataManager;

import java.util.ArrayList;

/**
 * Layer between View, DB and Model.
 * <p>
 * Created by Lettisia George on 01/10/2017.
 */

public class DatabaseController {
    private DatabaseHelper db;
    private Context context;

    public DatabaseController(Context context) {
        this.context = context;
        db = new DatabaseHelper(context);
    }

    public void setupDB() {
        FriendModel mFriendModel = FriendModel.getInstance();
        boolean isModelPopulated = (mFriendModel.getFriends().size() > 0);
        if (isModelPopulated) {
            return;
        }

        boolean hasFriends = db.checkFriendDB(mFriendModel.getFriends());
        //Add dummy data to Model
        if (!hasFriends) {
            mFriendModel.setFriends(DataManager.createDummyFriendList(context));
            //then store in DB directly
            int size = mFriendModel.getFriends().size();
            for(int i=0 ; i<size ; i++)
            {
                Friend friend = mFriendModel.getFriends().get(i);
                if(friend == null)
                    continue;

                db.addFriend(friend);
            }
        }

        MeetingModel mMeetingModel = MeetingModel.getInstance();
        boolean isMeetingModelPopulated = (mMeetingModel.getMeetings().size() > 0);
        if (isMeetingModelPopulated) {
            return;
        }

        boolean hasMeetings = db.checkMeetingDB(mMeetingModel.getMeetings());
        //Add dummy data to Model
        if (!hasMeetings) {
            mMeetingModel.setMeetings(DataManager.createDummMeetingList());
            //then store in DB directly
            int size = mMeetingModel.getMeetings().size();
            for(int i=0 ; i<size ; i++)
            {
                Meeting meeting = mMeetingModel.getMeetings().get(i);
                if(meeting == null)
                    continue;

                db.addMeeting(meeting);
            }
        }
        else //setting attendlist
        {
            int size = mMeetingModel.getMeetings().size();
            for (int i = 0; i < size; i++) {
                Meeting meeting = mMeetingModel.getMeetings().get(i);
                String meetingID = meeting.getID();
                ArrayList<String> attendIDList = db.readDB_AttendListTable(meetingID);
                for (int j = 0; j < attendIDList.size(); j++) {
                    Friend attend = mFriendModel.findFriendByID(attendIDList.get(j));
                    meeting.addAttend(attend);
                }
            }
        }
    }

    //need to update db when memory updated
    public void updateDB() {

    }

    public void closeDB() {
        //clean tables
        db.deleteRows();
        //saving data into db
        FriendModel mFModel = FriendModel.getInstance();
        MeetingModel mMModel = MeetingModel.getInstance();
        // set friends
        for (int i = 0; i < mFModel.getFriends().size(); i++) {
            db.addFriend(mFModel.getFriends().get(i));
        }
        // set meetings
        for (int j = 0; j < mMModel.getMeetings().size(); j++) {
            db.addMeeting(mMModel.getMeetings().get(j));
        }
    }
}
