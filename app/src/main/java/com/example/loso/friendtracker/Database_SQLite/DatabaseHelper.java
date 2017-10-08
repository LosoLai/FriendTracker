package com.example.loso.friendtracker.Database_SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.WalkTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Loso on 2017/9/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "FriendTrackerDB.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlFriendTable = "CREATE TABLE friend(friend_id TEXT PRIMARY KEY, name TEXT, email TEXT, date NUMERIC);";
        String sqlMeetingTable = "CREATE TABLE meeting(meeting_id TEXT PRIMARY KEY, title TEXT, start_date NUMERIC, end_date NUMERIC, location TEXT);";
        String sqlAttendList = "CREATE TABLE attendlist(meeting_id TEXT, friend_id TEXT,  " +
                "FOREIGN KEY(meeting_id) REFERENCES meeting(meeting_id), " +
                "FOREIGN KEY(friend_id) REFERENCES friend(friend_id));";

        db.execSQL(sqlFriendTable);
        db.execSQL(sqlMeetingTable);
        db.execSQL(sqlAttendList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlFriendTable = "DROP TABLE IF EXISTS friend";
        String sqlMeetingTable = "DROP TABLE IF EXISTS meeting";
        String sqlAttendList = "DROP TABLE IF EXISTS  attendlist";

        db.execSQL(sqlAttendList);
        db.execSQL(sqlMeetingTable);
        db.execSQL(sqlFriendTable);

        onCreate(db);
    }

    public boolean checkFriendDB(ArrayList<Friend> friendList) {
        Cursor cursor = readDB_FriendTable();
        if(cursor == null)
            return false;

        getFriends(cursor, friendList);
        return friendList.size() != 0;

    }

    private void getFriends(Cursor cursor, ArrayList<Friend> friendList) {
        if (friendList != null)
            friendList.clear();

        if (cursor.moveToFirst()) {
            do {
                long time = cursor.getLong(3);
                Date date = new Date(time);
                Friend friend = new Friend(cursor.getString(0), cursor.getString(1),
                                            cursor.getString(2), date);
                friendList.add(friend);
            } while (cursor.moveToNext());
        }
    }
    private Cursor readDB_FriendTable() {
        String selectQuery = "SELECT * FROM friend";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public boolean checkMeetingDB(ArrayList<Meeting> meetingList) {
        Cursor cursor = readDB_MeetingTable();
        if(cursor == null)
            return false;

        getMeetings(cursor, meetingList);
        if (meetingList.size() == 0)
            return false;

        return true;
    }
    private void getMeetings(Cursor cursor, ArrayList<Meeting> meetingList) {
        if(meetingList != null)
            meetingList.clear();

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                long startTime = cursor.getLong(2);
                long endTime = cursor.getLong(3);
                Date start = new Date();
                if(startTime != 0)
                    start.setTime(startTime);
                Date end = new Date();
                if(endTime != 0)
                    end.setTime(endTime);
                Meeting meeting = new Meeting(id, title);
                meeting.setStartDate(start);
                meeting.setEndDate(end);
                meetingList.add(meeting);
            } while (cursor.moveToNext());
        }
    }
    private Cursor readDB_MeetingTable() {
        String selectQuery = "SELECT * FROM meeting";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
    public ArrayList<String> readDB_AttendListTable(String meetingID) {
        String selectQuery = "SELECT * FROM attendlist WHERE meeting_id ='" + meetingID + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<String> attendList = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                // Adding attend to list
                attendList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return attendList;
    }

    public void deleteRows() {
        String sqlDeleteRows_Friend = "DELETE FROM friend";
        String sqlDeleteRows_Meeting = "DELETE FROM meeting";
        String sqlDeleteRows_Attend = "DELETE FROM attendlist";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sqlDeleteRows_Attend);
        db.execSQL(sqlDeleteRows_Meeting);
        db.execSQL(sqlDeleteRows_Friend);
        db.close();
    }

    public boolean addFriend(Friend friend) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("friend_id", friend.getID());
        contentValues.put("name", friend.getName());
        if (friend.getEmail() != null)
            contentValues.put("email", friend.getEmail());
        if (friend.getBirthday() != null)
            contentValues.put("date", friend.getBirthday().getTime());
        //contentValues.put("date", String.valueOf(friend.getBirthday()));
        db.insert("friend", null, contentValues);
        db.close();
        return true;
    }

    public boolean addMeeting(Meeting meeting) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("meeting_id", meeting.getID());
        contentValues.put("title", meeting.getTitle());
        if(meeting.getStartDate() != null)
            contentValues.put("start_date", meeting.getStartDate().getTime());
        if(meeting.getEndDate() != null)
            contentValues.put("end_date", meeting.getEndDate().getTime());
        //contentValues.put("start_date", String.valueOf(meeting.getStartDate()));
        //contentValues.put("end_date", String.valueOf(meeting.getEndDate()));
        if(meeting.getLocation() != null)
            contentValues.put("location", meeting.getLocation().toString());
        db.insert("meeting", null, contentValues);
        addAttendList(meeting);
        db.close();
        return true;
    }

    private boolean addAttendList(Meeting meeting) {
        SQLiteDatabase db = getWritableDatabase();
        HashMap<Friend, WalkTime> attendlist = meeting.getFriends();
        for (Friend attend : attendlist.keySet()) {
            if(attend == null)
                continue;
            ContentValues contentValues = new ContentValues();
            contentValues.put("meeting_id", meeting.getID());
            contentValues.put("friend_id", attend.getID());
            db.insert("attendlist", null, contentValues);
        }

        db.close();
        return true;
    }
}
