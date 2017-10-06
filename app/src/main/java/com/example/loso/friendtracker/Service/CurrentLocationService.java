package com.example.loso.friendtracker.Service;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.loso.friendtracker.Model.Location;

import java.util.Date;

/**
 * Contains and updates the current device location.
 *
 * Defaults to Location.RMIT
 *
 * Created by Lettisia George on 21/09/2017.
 *
 */

public class CurrentLocationService implements LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 33;
    private static final String LOG_TAG = "CurrentLocationService";
    private com.example.loso.friendtracker.Model.Location currentLocation = Location.RMIT;
    private Activity activity;

    public CurrentLocationService(Activity activity) throws SecurityException {
        this.activity = activity;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (checkPermissions()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            android.location.Location currLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currLoc != null) {
                saveLocationPreference(currLoc);
            } else {
                currentLocation = Location.RMIT;
            }
        } else {
            currentLocation = Location.RMIT;
        }
    }

    public com.example.loso.friendtracker.Model.Location getCurrentLocation() {
        Log.d(LOG_TAG, "getCurrentLocation()" + currentLocation.toString());
        return currentLocation;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        saveLocationPreference(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(LOG_TAG, "onStatusChanged()");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(LOG_TAG, "onProviderEnabled()");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(LOG_TAG, "onProviderDisabled()");
    }

    private boolean checkPermissions() {
        int permissionCheckFine = ActivityCompat.checkSelfPermission(activity,
                permission.ACCESS_FINE_LOCATION);
        int permissionCheckCoarse = ActivityCompat.checkSelfPermission(activity,
                permission.ACCESS_COARSE_LOCATION);

        if (permissionCheckFine != PackageManager.PERMISSION_GRANTED ||
                permissionCheckCoarse != PackageManager.PERMISSION_GRANTED) {
            //request permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        if (permissionCheckFine == PackageManager.PERMISSION_GRANTED &&
                permissionCheckCoarse == PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "checkPermissions() ok granted");
            return true;
        } else { //Still not granted
            Log.d(LOG_TAG, "checkPermissions() not granted");
            return false;
        }
    }

    private void saveLocationPreference(android.location.Location updatedLocation) {
        currentLocation.setLongitude(updatedLocation.getLongitude());
        currentLocation.setLatitude(updatedLocation.getLatitude());
        currentLocation.setTime(new Date(updatedLocation.getTime()));
        Log.d(LOG_TAG, "constructor()" + currentLocation.toString());

        SharedPreferences locationPref = activity.getSharedPreferences("location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = locationPref.edit();
        editor.putFloat("latitude", (float) currentLocation.getLatitude());
        editor.putFloat("longitude", (float) currentLocation.getLongitude());
        editor.putBoolean("changed", true);
        editor.apply();
    }
}
