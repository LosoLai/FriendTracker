package com.example.loso.friendtracker.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.R;

import java.util.ArrayList;

/**
 * Created by Loso on 2017/8/20.
 */

public class MeetingListAdapter extends ArrayAdapter<Meeting> {

    public MeetingListAdapter(Context context, ArrayList<Meeting> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        Meeting meeting = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_meeting_item, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitleContent);
        tvTitle.setText(meeting.getTitle());

        if(meeting.getLocation() != null)
        {
            TextView tvLocation = (TextView) view.findViewById(R.id.tvLocationContent);
            tvLocation.setText(meeting.getLocation().toString());
        }

        if(meeting.getFriends().size() > 0)
        {
            TextView tvAttend = (TextView) view.findViewById(R.id.tvAttendNumber);
            tvAttend.setText(Integer.toString(meeting.getFriends().size()));
        }
        return view;
    }
}
