package com.example.loso.friendtracker.Service;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.loso.friendtracker.Model.Location;

import java.util.Date;

/**
 * Created by Lettisia George on 21/09/2017.
 */

public class LocationService implements LocationListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 17;
    private static final String LOG_TAG = "LocationService";
    private com.example.loso.friendtracker.Model.Location currentLocation = new Location();
    private Activity activity;

    public LocationService(Activity activity) throws SecurityException {
        this.activity = activity;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (checkPermissions()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public com.example.loso.friendtracker.Model.Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.d(LOG_TAG, "onLocationChanged()");
        currentLocation.setTime(new Date(location.getTime()));
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
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
                    new String[]{Manifest.permission_group.LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            permissionCheckFine = ActivityCompat.checkSelfPermission(activity,
                    permission.ACCESS_FINE_LOCATION);
            permissionCheckCoarse = ActivityCompat.checkSelfPermission(activity,
                    permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionCheckFine == PackageManager.PERMISSION_GRANTED &&
                permissionCheckCoarse == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else { //Still not granted
            Toast.makeText(activity, "Requires access to location", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
