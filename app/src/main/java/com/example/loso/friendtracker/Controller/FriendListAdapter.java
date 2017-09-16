package com.example.loso.friendtracker.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.R;

import java.util.ArrayList;

/**
 * Created by Loso on 2017/8/20.
 */

public class FriendListAdapter extends ArrayAdapter<Friend> {

    public FriendListAdapter(Context context, ArrayList<Friend> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendController fc = new FriendController();
        fc.updateFriendLocations(this.getContext());

        // Get the data item
        Friend friend = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_friend_item, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(friend.getName());

        //Location fl = friend.getLocation();
        Location fl = FriendController.getFriendLocationsForTime(this.getContext(), friend.getName());

        if (fl != null)
        {
            Log.d("FriendListAdapter", fl.toString());
            TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);

            tvLocation.setText("(" + fl.getLatitude() + ", " + fl.getLongitude() + ")");
        }

        return view;
    }
}
