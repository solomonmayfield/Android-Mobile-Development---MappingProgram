package com.example.mappingprogram;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationTrackerManager {
    private static final String TAG = "LocationTrackerManager";
    public static final String ACTION_LOCATION = 
    		"com.bignerdranch.android.runtracker.ACTION_LOCATION";
    private static final String TEST_PROVIDER = "TEST_PROVIDER";
    
    private static LocationTrackerManager locatationTrackerManager;
    private Context appContext;
    private LocationManager locationManager;
    
    private LocationTrackerManager(Context appContext) {
        this.appContext = appContext;
        locationManager = (LocationManager)appContext
        		.getSystemService(Context.LOCATION_SERVICE);
    }
    
    public static LocationTrackerManager get(Context c) {
        if (locatationTrackerManager == null) {
            // we use the application context to avoid leaking activities
            locatationTrackerManager = 
            		new LocationTrackerManager(c.getApplicationContext());
        }
        return locatationTrackerManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(appContext, 0, broadcast, flags);
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;
        Log.d(TAG, "Using provider " + provider);

        // get the last known location and broadcast it if we have one
        Location lastKnown = locationManager.getLastKnownLocation(provider);
        if (lastKnown != null) {
            // reset the time to now
            lastKnown.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnown);
        }
        // start updates from the location manager
        PendingIntent pi = getLocationPendingIntent(true);
        locationManager.requestLocationUpdates(provider, 0, 0, pi);
    }
    
    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            locationManager.removeUpdates(pi);
            pi.cancel();
        }
    }
    
    public boolean isTracking() {
        return getLocationPendingIntent(false) != null;
    }
    
    private void broadcastLocation(Location location) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        appContext.sendBroadcast(broadcast);
    }
}
