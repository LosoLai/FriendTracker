package com.example.loso.friendtracker.View;

/**
 * Created by Loso on 2017/8/19.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Model.MeetingModel;
import com.example.loso.friendtracker.R;

import com.example.loso.friendtracker.Controller.MeetingListAdapter;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.MeetingComparator;

import java.util.Collections;
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
        MeetingModel mMeetingModel = MeetingModel.getInstance();
        mMeetingModel.addObserver(this);
        final ArrayList<Meeting> meetings = mMeetingModel.getMeetings();
        adapter = new MeetingListAdapter(rootView.getContext(), meetings);
        ListView lvMeeting = (ListView) rootView.findViewById(R.id.meetinglist);
        lvMeeting.setAdapter(adapter);

        lvMeeting.setClickable(true);
        lvMeeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meeting meeting = adapter.getItem(position);
                startActivity(new Intent(getActivity(), EditMeetingActivity.class).putExtra("meeting", meeting.getID()));
            }
        });


        lvMeeting.setLongClickable(true);
        lvMeeting.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Meeting meeting = adapter.getItem(position);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Remove Meeting")
                        .setMessage("Do you really want to remove this meeting?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                MeetingController mc = new MeetingController();
                                mc.removeMeeting(meeting.getID());
                                Toast.makeText(getActivity(), "Meeting removed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });

        // add by Loso 09.09.17
        // sort buttons
        Button acsBtn = (Button) rootView.findViewById(R.id.btnAcs);
        acsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingComparator comp = new MeetingComparator(MeetingComparator.ORDER_ACS);
                Collections.sort(meetings, comp);
                adapter.notifyDataSetChanged();
            }
        });

        Button decsBtn = (Button) rootView.findViewById(R.id.btnDecs);
        decsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingComparator comp = new MeetingComparator(MeetingComparator.ORDER_DECS);
                Collections.sort(meetings, comp);
                adapter.notifyDataSetChanged();
            }
        });
        //---------------------------
    }

    @Override
    public void update(Observable o, Object arg) {
        adapter.notifyDataSetChanged();
    }
}
