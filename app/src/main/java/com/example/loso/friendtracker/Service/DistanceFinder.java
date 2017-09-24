package com.example.loso.friendtracker.Service;

import android.app.Activity;

import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.http.HttpAsyncTask;

/**
 * Contains the JSON stuff and creates URLs to query the Google Distance Matrix API
 * <p>
 * Created by letti on 24/09/2017.
 */

public class DistanceFinder {
    private static final String ROOT_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=walking";
    private Activity activity;

    public DistanceFinder(Activity activity) {
        this.activity = activity;
    }

    public void getWalkingTime(Location start, Location[] finish) throws Exception {
        double[] times = new double[finish.length];

        StringBuilder sb = new StringBuilder(ROOT_URL);

        sb.append("&origins=" + start.getLatitude() + "," + start.getLongitude());
        sb.append("&destinations=");
        for (Location loc : finish) {
            sb.append(loc.getLatitude() + "," + loc.getLongitude() + "|");
        }
        // remove last | pipe symbol
        sb.deleteCharAt(sb.length() - 1);

        if (sb.length() >= 8192) {
            throw new Exception("URL string is too long. Use fewer locations.");
        }

        HttpAsyncTask task = new HttpAsyncTask(activity, sb.toString());

    }

    public void parseWalkTimes(String json) {

    }
}
