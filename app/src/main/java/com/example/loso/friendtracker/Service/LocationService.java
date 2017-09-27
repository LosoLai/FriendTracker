package com.example.loso.friendtracker.Service;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.R;

import java.util.Date;

/**
 * Created by Lettisia George on 21/09/2017.
 */

public class LocationService implements LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 33;
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

        TextView tvCurrent = (TextView) activity.findViewById(R.id.tvCurrentLocation);
        tvCurrent.setText(currentLocation.toString());
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
}
