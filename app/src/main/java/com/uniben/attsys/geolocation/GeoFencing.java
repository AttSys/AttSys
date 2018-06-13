package com.uniben.attsys.geolocation;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.uniben.attsys.R;
import com.uniben.attsys.models.Venue;
import com.uniben.attsys.utils.Constants;
import com.uniben.attsys.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

public class GeoFencing implements OnCompleteListener<Void> {

    // Constants
    public static final String TAG = GeoFencing.class.getSimpleName();
    private static final float GEOFENCE_RADIUS = 50; // 50 meters
    private static final long GEOFENCE_TIMEOUT = 24 * 60 * 60 * 1000; // 24 hours

    private List<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private GeofencingClient mGeoFencingClient;
    private Activity activity;
    private Venue venue;
    private LocationRequest request;
    private GeoFencingListener geoFencingListener;

    public GeoFencing(Activity context, Venue venue, GeoFencingListener listener) {
        activity = context;
        this.venue = venue;
        mGeoFencingClient = LocationServices.getGeofencingClient(context.getApplicationContext());
        mGeofencePendingIntent = null;
        this.geoFencingListener = listener;
        mGeofenceList = new ArrayList<>();
    }


    public void configureLocationSettings() {
        initializeLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(activity, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            Log.i(TAG, "onSuccess: Location");
          if(doCheckLocationPermission()) return;

            updateGeoFencesList();
            registerAllGeoFences();

        });

        task.addOnFailureListener(activity, e -> {
            Log.e(TAG, "onFailure: " + e.getMessage());
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                Log.e(TAG, "onFailure rESOLVE: ");
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity,
                            Constants.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                    Log.e(TAG, "onFailure: " + sendEx.getMessage() );
                }
            }
        });
    }

    private void initializeLocationRequest() {
        request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(10000);
        request.setFastestInterval(5000);
    }

    private void registerAllGeoFences() {
        // Check that the API client is connected and that the list has Geofences in it
        if (mGeoFencingClient == null ||
                mGeofenceList == null || mGeofenceList.size() == 0) {
            return;
        }

        if (doCheckLocationPermission()) return;

        try {

            mGeoFencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnCompleteListener(this);
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.e(TAG, securityException.getMessage());
        }
    }

    private boolean doCheckLocationPermission() {
        if (!checkPermissions()) {
            NotificationUtils.notifyUser(activity, activity.getString(R.string.insufficient_permissions));
            geoFencingListener.onRequestPermission();
            return true;
        }
        return false;
    }

    public void unRegisterAllGeofences() {
        if (mGeoFencingClient == null) {
            return;
        }

        if (doCheckLocationPermission()) return;

        mGeoFencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){


                    int messageId = R.string.geofences_removed;

                    Log.i(TAG, "onComplete: REMOVED" + activity.getString(messageId));
//            Log.i(TAG, "onComplete: ADDED = " + getGeofencesAdded());
                    geoFencingListener.onUpdateGeoFence(false);

                } else {
                    // Get the status code for the error and log it using a user-friendly message.
                    String errorMessage = GeofenceErrorMessages.getErrorString(activity, task.getException());
                    geoFencingListener.onError(errorMessage);
                    Log.w(TAG, errorMessage);
                }
            }
        });
    }


    /***
     * Updates the local ArrayList of Geofences using data from the passed in list
     * Uses the Venue ID  as the Geofence object Id
     *
     */
    public void updateGeoFencesList() {
        mGeofenceList = new ArrayList<>();
        if (venue == null ) return;
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(venue.getName())
                    .setCircularRegion(
                            venue.getLatitude(),
                            venue.getLongitude(),
                            venue.getRadius()
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setNotificationResponsiveness(1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT).build();
        Log.i(TAG, "updateGeoFencesList: " + venue.getLatitude() + ", " + venue.getLongitude()+ ". " + venue.getRadius());
            mGeofenceList.add(geofence);

    }

    /***
     * Creates a GeofencingRequest object using the mGeofenceList ArrayList of Geofences
     * Used by {@code #registerGeofences}
     *
     * @return the GeofencingRequest object
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER );
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(activity, GeoFencingIntentService.class);
        mGeofencePendingIntent = PendingIntent.getService(activity, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }




    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Returns true if geofences were added, otherwise false.
     */
    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

    /**
     * Stores whether geofences were added ore removed in {@link SharedPreferences};
     *
     * @param added Whether geofences were added or removed.
     */
    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(activity)
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }


    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
//            updateGeofencesAdded(!getGeofencesAdded());

            int messageId = R.string.geofences_added;

            Log.i(TAG, "onComplete: " + activity.getString(messageId));
//            Log.i(TAG, "onComplete: ADDED = " + getGeofencesAdded());
            geoFencingListener.onUpdateGeoFence(true);

        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(activity, task.getException());
            geoFencingListener.onError(errorMessage);
            Log.w(TAG, errorMessage);
        }
    }

}
