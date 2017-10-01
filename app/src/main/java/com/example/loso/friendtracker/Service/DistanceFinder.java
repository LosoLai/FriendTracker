package com.example.loso.friendtracker.Service;

import android.util.Log;

import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.WalkTime;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Contains the JSON stuff and creates URLs to query the Google Distance Matrix API
 * Cannot be run on main thread.
 * <p>
 * Created by letti on 24/09/2017.
 */

public class DistanceFinder {
    private static final String LOG_TAG = "DistanceFinder";
    private static final String ROOT_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=walking";

    public String accessURL(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
        String responseBody = null;

        try {
            // get the result
            responseBody = httpclient.execute(getRequest,
                    new BasicResponseHandler());
        } catch (Exception e) {
            // Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
        Log.d(LOG_TAG, responseBody != null ? responseBody : "responseBody is null");
        return responseBody;
    }


    public WalkTime parseWalkTime(String JSONresponse) {
        WalkTime walkTime = new WalkTime();
        if (JSONresponse != null) {
            try {
                JSONObject duration = new JSONObject(JSONresponse)
                        .getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("elements")
                        .getJSONObject(0)
                        .getJSONObject("duration");
                walkTime = new WalkTime(duration.getDouble("value"), duration.getString("text"));
                Log.d(LOG_TAG, "Walk time = " + walkTime);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        return walkTime;
    }

    public String generateDistanceURL(Location start, Location finish) {
        String str = String.format(Locale.ENGLISH,
                "&origins=%f,%f&destinations=%f,%f",
                start.getLatitude(),
                start.getLongitude(),
                finish.getLatitude(),
                finish.getLongitude());
        return ROOT_URL + str;
    }
}
