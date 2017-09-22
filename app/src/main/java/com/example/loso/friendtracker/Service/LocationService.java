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
import android.widget.Toast;

import com.example.loso.friendtracker.Model.Location;

/**
 * Created by letti on 21/09/2017.
 */

public class LocationService implements LocationListener {
    private com.example.loso.friendtracker.Model.Location currentLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private android.location.Location location;
    private Activity activity;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 17;

    public LocationService(Activity activity) throws SecurityException {
        this.activity = activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (checkPermissions()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public com.example.loso.friendtracker.Model.Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
