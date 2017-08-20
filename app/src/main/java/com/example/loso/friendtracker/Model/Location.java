package com.example.loso.friendtracker.Model;

import java.util.Random;

/**
 * Repersents a location by latitude and longitude and can generate a random location
 *
 * @author Lettisia George
 */

public class Location {
    // LatLong of RMIT: -37.809427, 144.963727
    private double latitude;
    private double longitude;

    public Location(double lat, double longi) {
        latitude = lat;
        longitude = longi;
    }

    public double distance(Location there) {
        double lat1 = Math.toRadians(latitude);
        double long1 = Math.toRadians(longitude);
        double lat2 = Math.toRadians(there.getLatitude());
        double long2 = Math.toRadians(there.getLongitude());

        // Use spherical cosine formula to calculate distance in km
        // See http://www.movable-type.co.uk/scripts/latlong.html
        return Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1))
                * 6371; // Radius of Earth in km
    }

    /**
     * Generates a random location. The new location will be within a square
     * with sides of length 2*within and Location 'near' in the centre.
     *
     * @param near   a Location
     * @param within in degrees
     */
    public static Location generateRandomLocation(Location near, double within) {
        Random rand = new Random(System.currentTimeMillis());
        double lat = near.getLatitude() + rand.nextDouble() * 2 * within - within;
        double longi = near.getLongitude() + rand.nextDouble() * 2 * within - within;
        return new Location(lat, longi);
    }

	/*
     * Simple distance to degrees conversion that is approximate near Melbourne
	 * and worse elsewhere. 
	 * 
	 * How I found the magic number 0.01:
	 * 
	 * I found the factor of 0.01 by starting with the
	 * coordinates of RMIT, adding 0.01 to the latitude and longitude
	 * respectively and finding the average distance between RMIT and the new
	 * points 0.01 degrees away. Changing the latitude by 0.01 degrees gives a
	 * distance of 1.11km. Changing the longitude by 0.01 degrees gives a
	 * distance of 0.87. so 0.01 degrees/km seems reasonable.
	 */
    public static double distanceToDegrees(double distance) {
        return distance * 0.01;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Test method
    public static void main(String[] args) {
        Location loc1 = new Location(-37.809427, 144.963727);
        Location loc2 = new Location(-38.809427, 144.963727);
        Location loc3 = new Location(-37.809427, 144.953727);
        Location loc4 = new Location(-37.819427, 144.973727);

        System.out.println("Changing Latitude " + loc1.distance(loc2));
        System.out.println("Changing Longitude " + loc1.distance(loc3));
        System.out.println("Changing Both " + loc1.distance(loc4));
        System.out.println("Distance: 100km = " + distanceToDegrees(100) + " degrees");
    }

}
