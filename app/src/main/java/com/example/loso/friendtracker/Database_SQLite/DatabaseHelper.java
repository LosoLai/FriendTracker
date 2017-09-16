package com.example.loso.friendtracker.Database_SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Meeting;

import java.util.List;

/**
 * Created by Loso on 2017/9/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "FriendTrackerDB.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public boolean test(String str)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", str);
        db.insert("artists", null, contentValues);
        db.close();
        return true;
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
        String sqlTable = "DROP TABLE IF EXISTS artists";
        db.execSQL(sqlTable);
        onCreate(db);
        String sqlFriendTable = "DROP TABLE IF EXISTS friend";
        String sqlMeetingTable = "DROP TABLE IF EXISTS meeting";
        String sqlAttendList = "DROP TABLE IF EXISTS  attendlist";

        db.execSQL(sqlAttendList);
        db.execSQL(sqlMeetingTable);
        db.execSQL(sqlFriendTable);

        onCreate(db);
    }

    public boolean addFriend(Friend friend) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("friend_id", friend.getID());
        contentValues.put("name", friend.getName());
        contentValues.put("email", friend.getEmail());
        contentValues.put("date", String.valueOf(friend.getBirthday()));
        db.insert("friend", null, contentValues);
        db.close();
        return true;
    }

    public boolean addMeeting(Meeting meeting) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("meeting_id", meeting.getID());
        contentValues.put("title", meeting.getTitle());
        contentValues.put("start_date", String.valueOf(meeting.getStartDate()));
        contentValues.put("end_date", String.valueOf(meeting.getEndDate()));
        contentValues.put("location", meeting.getLocation().toString());
        db.insert("meeting", null, contentValues);
        db.close();
        return true;
    }

    public boolean addAttendList(Meeting meeting) {
        SQLiteDatabase db = getWritableDatabase();
        List<Friend> attendlist = meeting.getFriends();
        for(int i=0 ; i<attendlist.size() ; i++)
        {
            Friend attend = attendlist.get(i);
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
