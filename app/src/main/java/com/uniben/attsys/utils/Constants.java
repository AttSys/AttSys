package com.uniben.attsys.utils;

import com.google.android.gms.maps.model.LatLng;
import com.uniben.attsys.BuildConfig;

import java.util.HashMap;

/**
 * Created by Cyberman on 5/30/2018.
 */

public class Constants {
    public static final String BASE_URL = BuildConfig.BASE_URL;

    public static final String USER_KEY = "user_key";
    public static final long EXIT_APP_DURATION = 2000;



    private static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile, 1.6 km

    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<>();
    public static final String STUDENT_KEY = "STUDENT_KEY";
    public static final String ATTENDANCE_KEY = "ATTENDANCE_KEY";
    public static final int REQUEST_CHECK_SETTINGS = 1022;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 122;

    static {
        // San Francisco International Airport.
        BAY_AREA_LANDMARKS.put("SFO", new LatLng(37.621313, -122.378955));

        // Googleplex.
        BAY_AREA_LANDMARKS.put("GOOGLE", new LatLng(37.422611,-122.0840577));
    }
}
