package com.example.loso.friendtracker.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
 * @author LosoLai
 * @author Lettisia George
 */

public class FriendListAdapter extends ArrayAdapter<Friend> {
    private static final String LOG_TAG = "FriendListAdapter";
    private Location currentLocation = Location.RMIT;

    public FriendListAdapter(Context context, ArrayList<Friend> contacts) {
        super(context, 0, contacts);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("location", Context.MODE_PRIVATE);
        double lati = sharedPreferences.getFloat("latitude", (float) currentLocation.getLatitude());
        double longi = sharedPreferences.getFloat("longitude", (float) currentLocation.getLongitude());
        currentLocation.setLongitude(longi);
        currentLocation.setLatitude(lati);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FriendController fc = new FriendController();
        //fc.updateFriendLocations(this.getContext());

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

        if (!friend.getWalkTime().invalid()) {
            TextView tvWalkTime = (TextView) view.findViewById(R.id.tvWalkTime);
            tvWalkTime.setText(friend.getWalkTime().getStringTime());
        }

        //Location fl = friend.getLocation();
        Location fl = fc.getFriendLocationsForTime(this.getContext(), friend.getID());

        if (fl != null) {
            TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            Log.d("FriendListAdapter", fl.toString());
            tvLocation.setText("(" + fl.getLatitude() + ", " + fl.getLongitude() + ")");
            double distance = currentLocation.distance(fl);
            TextView tvDistance = (TextView) view.findViewById(R.id.tvDistance);
            String text = String.format("%.3f %s", distance, "km");
            tvDistance.setText(text);
            Log.d(LOG_TAG, text);
        }
        return view;
    }
}
