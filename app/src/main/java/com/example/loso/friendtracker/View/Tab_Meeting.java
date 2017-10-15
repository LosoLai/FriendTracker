package com.example.loso.friendtracker.View;

/**
 * Created by Loso on 2017/8/19.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loso.friendtracker.Alarm.AlarmSuggestionReceiver;
import com.example.loso.friendtracker.Alarm.MeetingSuggestionManager;
import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Controller.MeetingListAdapter;
import com.example.loso.friendtracker.Controller.PreferenceController;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.MeetingComparator;
import com.example.loso.friendtracker.Model.MeetingModel;
import com.example.loso.friendtracker.R;
import com.example.loso.friendtracker.Service.MeetingSuggestionController;
import com.example.loso.friendtracker.Service.NetworkStatusReceiver;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class Tab_Meeting extends Fragment implements Observer {
    private static final String LOG_TAG = "meetingtab";
    private static boolean isVisible = true;
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

        // This code chunk could potentially be moved to where meeting suggestion functionality is:
        IntentFilter intentFilter1 = new IntentFilter(NetworkStatusReceiver.NETWORK_CHANGE_DETECTED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(LOG_TAG, "entered onReceive()");
                boolean connected = intent.getBooleanExtra(NetworkStatusReceiver.IS_NETWORK_CONNECTED, false);

                String text = connected ? "Network Connected" : "Network Disconnected";
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                if (connected) {
                    MeetingSuggestionController suggestionController = MeetingSuggestionController.getInstance();
                    Meeting suggest = suggestionController.getSuggestion();

                    if (isVisible) {
                        setPopUpWindow(suggest);
                    }
                }
                Log.d(LOG_TAG, text);
            }
        }, intentFilter1);


        // ListView
        MeetingModel mMeetingModel = MeetingModel.getInstance();
        mMeetingModel.addObserver(this);
        final ArrayList<Meeting> meetings = mMeetingModel.getMeetings();
        adapter = new MeetingListAdapter(rootView.getContext(), meetings);
        ListView lvMeeting = (ListView) rootView.findViewById(R.id.meetinglist);
        lvMeeting.setAdapter(adapter);

        //MeetingSuggestionController meetCon = new MeetingSuggestionController(getContext());
        //meetCon.getWalkingTime(Location.RMIT, Location.NEAR_RMIT);

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

        Button btnSuggestion = (Button) rootView.findViewById(R.id.btnSuggestion);
        btnSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingController meetingController = new MeetingController();
                Meeting suggest = meetingController.createTempMeeting();

                //get current location
                SharedPreferences pref = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
                LatLng here = new LatLng(pref.getFloat("latitude", (float) Location.RMIT.getLatitude()),
                        pref.getFloat("longitude", (float) Location.RMIT.getLongitude()));
                Location current = new Location(here);

                PreferenceController preferenceController = PreferenceController.getInstance();
                preferenceController.setCurrentLocation(current);

                //active suggestion alarm
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(), AlarmSuggestionReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), AlarmSuggestionReceiver.ALARM_SUGGESTION_ID, intent, 0);
                MeetingSuggestionManager meetingSuggestionManager = MeetingSuggestionManager.getInstance();
                meetingSuggestionManager.enableMeetingSuggestion(alarmManager, intent, pendingIntent);

                //MeetingSuggestionController
                MeetingSuggestionController suggestionController = MeetingSuggestionController.getInstance();
                suggest = suggestionController.getSuggestion();

                setPopUpWindow(suggest);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isVisible = false;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d(LOG_TAG, "MADE IT TO UPDATE METHOD.");
        adapter.notifyDataSetChanged();
    }

    public void setPopUpWindow(final Meeting suggestion) {
        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_suggestion, null);

        Button accept = (Button) customView.findViewById(R.id.btnAccept);
        Button ignore = (Button) customView.findViewById(R.id.btnIgnore);

        //instantiate popup window
        final PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (suggestion != null)
            displaySuggestionInfo(customView, suggestion);
        popupWindow.update();

        //display the popup window
        LinearLayout linearLayout1 = (LinearLayout) rootView.findViewById(R.id.linearLayout_meeting);
        popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suggestion != null) {
                    MeetingController meetingController = new MeetingController();
                    meetingController.addMeetingIntoList(suggestion);

                    popupWindow.dismiss();
                }
            }
        });
    }

    public void displaySuggestionInfo(View customView, Meeting meeting) {
        TextView title = (TextView) customView.findViewById(R.id.text_suggest_title);
        TextView location = (TextView) customView.findViewById(R.id.text_suggest_location);
        TextView attend = (TextView) customView.findViewById(R.id.text_suggest_attend);
        TextView startDate = (TextView) customView.findViewById(R.id.text_suggest_startdate);

        if (title != null && meeting.getTitle() != null)
            title.append(meeting.getTitle());

        if (location != null && meeting.getLocation() != null)
            location.append(meeting.getLocation().toString());

        if (attend != null && meeting.getFriends() != null) {
            Iterator<Friend> itr = meeting.getFriends().keySet().iterator();
            if (itr.hasNext()) {
                Friend friend = itr.next();
                attend.append(friend.getName());
            }
        }

        if (startDate != null && meeting.getStartDate() != null)
            startDate.append(meeting.getStartDate().toString());
    }

}
