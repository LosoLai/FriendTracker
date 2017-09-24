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
import com.example.loso.friendtracker.Service.LocationService;

import java.util.ArrayList;

/**
 * Created by Loso on 2017/8/20.
 */

public class FriendListAdapter extends ArrayAdapter<Friend> {
    LocationService locationService;

    public FriendListAdapter(Context context, ArrayList<Friend> contacts) {
        super(context, 0, contacts);
        locationService = null;
    }

    public FriendListAdapter(Context context, ArrayList<Friend> contacts, LocationService locationService) {
        super(context, 0, contacts);
        this.locationService = locationService;
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
        Location currentLocation;

        if (fl != null) {
            TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            Log.d("FriendListAdapter", fl.toString());
            tvLocation.setText("(" + fl.getLatitude() + ", " + fl.getLongitude() + ")");
            // Get current Location
            if (locationService != null) {
                currentLocation = locationService.getCurrentLocation();
                double distance = currentLocation.distance(fl);

                TextView tvDistance = (TextView) view.findViewById(R.id.tvDistance);
                tvDistance.setText(distance + "km");
            }
        }
        return view;
    }
}
