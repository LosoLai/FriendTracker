package com.example.loso.friendtracker.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.WalkTime;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * CLass to automatically update the walk times for friends.
 * <p>
 * Created by Lettisia George on 29/09/2017.
 */

public class DistanceService extends IntentService {
    private static final String LOG_TAG = "DistanceService";
    private static final String SERVICE_NAME = "DistanceService";
    private static final String ROOT_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=walking";

    private Location currentLocation = Location.RMIT;
    private FriendController friendController;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DistanceService() {
        super(SERVICE_NAME);
        friendController = new FriendController();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent()");
        ArrayList<Friend> friends = friendController.getFriendsList();
        for (Friend friend : friends) {
            generateFriendWalkTimes(friend);
        }

        // hard coded friend for testing
        // generateFriendWalkTimes(new Friend(FriendModel.createID(), "john","email"));
    }


    private void updateCurrentLocation() {
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences("location", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("changed", true)) {
            double lati = sharedPreferences.getFloat("latitude", (float) currentLocation.getLatitude());
            double longi = sharedPreferences.getFloat("longitude", (float) currentLocation.getLongitude());
            currentLocation.setLongitude(longi);
            currentLocation.setLatitude(lati);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("changed", false);
            edit.apply();
        }
        Log.d(LOG_TAG, "Current Location Updated to: " + currentLocation.toString());
    }


    public void generateFriendWalkTimes(Friend friend) {
        String walkString = "";
        updateCurrentLocation();
        friendController.updateFriendLocations(getApplicationContext());
        Location friendLocation = friend.getLocation();
        if (friendLocation != null) {
            String url = generateDistanceURL(currentLocation, friendLocation);
            Log.d(LOG_TAG, "URL: " + url);
            WalkTime walkTime = parseWalkTime(accessURL(url));
            friendController.setWalkTime(friend.getID(), walkTime);
        }
    }


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
        StringBuilder sb = new StringBuilder(ROOT_URL);
        sb.append("&origins=");
        sb.append(start.getLatitude());
        sb.append(",");
        sb.append(start.getLongitude());
        sb.append("&destinations=");
        sb.append(finish.getLatitude());
        sb.append(",");
        sb.append(finish.getLongitude());
        return sb.toString();
    }

}
