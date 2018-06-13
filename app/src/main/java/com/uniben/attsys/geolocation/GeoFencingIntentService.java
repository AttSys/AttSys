package com.uniben.attsys.geolocation;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.uniben.attsys.AttendanceTakenActivity;
import com.uniben.attsys.R;

import java.util.ArrayList;
import java.util.List;

public class GeoFencingIntentService extends IntentService {
    private static final String TAG = "GeoFencingIntentService";
    public static final String ACTION_WITHIN_GEOFENCE  = "com.uniben.attsys.geolocation.ACTION_WITHIN_GEOFENCE";
    public static final String ACTION_OUTSIDE_GEOFENCE  = "com.uniben.attsys.geolocation.ACTION_OUTSIDE_GEOFENCE";
    public static final String ACTION_ERROR_GEOFENCE = "com.uniben.attsys.geolocation.ACTION_ERROR_GEOFENCE";
    public static final String MESSAGE_KEY = "com.uniben.attsys.geolocation.MESSAGE_KEY";


    public GeoFencingIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onReceive: GEO" );
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            sendBroadCast(ACTION_ERROR_GEOFENCE, errorMessage);
            Log.e(TAG, errorMessage);

            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
              ) {

//            listener.onSuccess(true);

            Log.w(TAG, "onHandleIntent: TRIGGERED "  );
            Intent i = new Intent(this, AttendanceTakenActivity.class);
            startActivity(i);
            sendBroadCast(ACTION_WITHIN_GEOFENCE, null);
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,
                    triggeringGeofences, this);

            Log.w(TAG, geofenceTransitionDetails);
        }else if(  geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.e(TAG, "onHandleIntent: TRIGGERED_EXIT "  );
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,
                    triggeringGeofences, this);
            sendBroadCast(ACTION_OUTSIDE_GEOFENCE, null);
            Log.e(TAG, geofenceTransitionDetails);
        }
            else
         {
            // Log the error.
//            listener.onSuccess(false);
             sendBroadCast(ACTION_ERROR_GEOFENCE, "An error occurred! Please try again");
            Log.e(TAG, this.getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }


    private void sendBroadCast(String action, String message){
        Intent intent = new Intent(action);
        intent.putExtra(MESSAGE_KEY, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences, Context context) {

        String geofenceTransitionString = getTransitionString(geofenceTransition, context);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }



    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType, Context context) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return context.getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return context.getString(R.string.geofence_transition_exited);
            default:
                return context.getString(R.string.unknown_geofence_transition);
        }
    }
}
