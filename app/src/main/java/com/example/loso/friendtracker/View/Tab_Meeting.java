package com.example.loso.friendtracker.View;

/**
 * Created by Loso on 2017/8/19.
 */
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.loso.friendtracker.R;

import com.example.loso.friendtracker.Controller.MeetingListAdapter;
import com.example.loso.friendtracker.Model.Model;
import com.example.loso.friendtracker.Model.Meeting;

import java.util.Observer;
import java.util.Observable;
import java.util.ArrayList;

public class Tab_Meeting extends Fragment implements Observer {
    private static final String LOG_TAG = "meetingtab";
    private View rootView;
    private MeetingListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_meeting, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ListView
        Model mModel = Model.getInstance();
        mModel.addObserver(this);
        ArrayList<Meeting> meetings = mModel.getMeetings();
        adapter = new MeetingListAdapter(rootView.getContext(), meetings);
        ListView lvMeeting = (ListView) rootView.findViewById(R.id.meetinglist);
        lvMeeting.setAdapter(adapter);
        lvMeeting.setLongClickable(true);
        lvMeeting.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Meeting meeting = adapter.getItem(position);
                startActivity(new Intent(getActivity(), EditMeetingActivity.class).putExtra("meeting", meeting.getID()));
                return true;
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals(Model.MEETINGS_CHANGED)) {
            adapter.notifyDataSetChanged();
        } else if (arg instanceof Meeting) { // srg is a friend that has been removed
            adapter.remove((Meeting) arg);
            adapter.notifyDataSetChanged();
        }
    }
}
