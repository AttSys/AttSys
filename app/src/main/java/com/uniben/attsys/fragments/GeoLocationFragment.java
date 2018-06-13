package com.uniben.attsys.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todddavies.components.progressbar.ProgressWheel;
import com.uniben.attsys.AttendanceTakenActivity;
import com.uniben.attsys.BuildConfig;
import com.uniben.attsys.R;
import com.uniben.attsys.geolocation.GeoFencing;
import com.uniben.attsys.geolocation.GeoFencingListener;
import com.uniben.attsys.geolocation.GeofenceBroadcastReceiver;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.utils.Constants;
import com.uniben.attsys.utils.NotificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Cyberman on 6/6/2018.
 */

public class GeoLocationFragment extends Fragment implements GeofenceBroadcastReceiver.OnGeoFenceListener, GeoFencingListener {
    private static final String TAG = "GeoLocationFragment";
    private GeoFencing geoFencing;
    private GeofenceBroadcastReceiver receiver;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    private Attendance attendance;

    public static GeoLocationFragment newInstance(Attendance attendance) {
        Bundle args = new Bundle();
        args.putParcelable(TAG, attendance);
        GeoLocationFragment fragment = new GeoLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         attendance = getArguments().getParcelable(TAG);

        receiver = new GeofenceBroadcastReceiver();
        receiver.setListener(this);
        geoFencing = new GeoFencing(getActivity(), attendance.getVenue(), this);
        geoFencing.configureLocationSettings();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geofence, container, false);
        ButterKnife.bind(this, view);
        Log.i(TAG, "onCreateView: Name-> " + attendance.getName());
        Log.i(TAG, "onCreateView: COURSE_NAME ->" + attendance.getCourse().getName());
        return view;
    }


    public void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
                    });
        } else {
            Log.i(TAG, "Requesting permission");

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        if (getView() != null) {
            Snackbar.make(
                    getView(),
                    getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode ==Constants.REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK){
            geoFencing.configureLocationSettings();
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {

                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
                    geoFencing.configureLocationSettings();
            } else {

                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        progressWheel.startSpinning();
        getActivity().registerReceiver(receiver, new IntentFilter());
    }


    @Override
    public void onPause() {
        super.onPause();
        progressWheel.stopSpinning();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        geoFencing.unRegisterAllGeofences();
        geoFencing = null;
    }

    @Override
    public void onSuccess(boolean isSucess) {
        if(isSucess){
            NotificationUtils.notifyUser(getView(), "Attendance Success");
            Intent intent = new Intent(getContext(), AttendanceTakenActivity.class);
            startActivity(intent);
        }else {
            NotificationUtils.notifyUser(getView(), "Attendance Failure");
        }

    }

    @Override
    public void onRequestPermission() {
        requestPermissions();
    }

    @Override
    public void onUpdateGeoFence(boolean added) {
        if (getView() != null) {
            if(added){
                NotificationUtils.notifyUser(getView(), "Confirming location");
            }else{
                NotificationUtils.notifyUser(getView(), "Location updates removed");
            }

        }
    }

    @Override
    public void onError(String message) {
        NotificationUtils.notifyUser(getView(), message + " try again");
    }
}
