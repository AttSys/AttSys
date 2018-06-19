
package com.uniben.attsys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.uniben.attsys.fragments.GeoLocationFragment;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.utils.ActivityUtils;
import com.uniben.attsys.utils.Constants;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = AttendanceActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public double LAT = 6.43238250000001;
    public double LNG = 5.587714843750021;


    public static final String CURRENT_LOCATION = "6.4021796, 5.6186157 ";
    private Attendance attendance;
    private GeoLocationFragment geoLocationFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        attendance = getIntent().getParcelableExtra(Constants.ATTENDANCE_KEY);

        initFragment(LAT, LNG);

    }

    private void initFragment(double lat, double lng) {
        geoLocationFragment = (GeoLocationFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (geoLocationFragment == null) {
               /* attendance.getVenue().setLatitude(lat);
                attendance.getVenue().setLongitude(lng);
                attendance.getVenue().setRadius(10000);*/
                geoLocationFragment =  GeoLocationFragment.newInstance(attendance);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        geoLocationFragment, R.id.fragment_container);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test_location, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return  true;
        }else if(item.getItemId() == R.id.action_test_location){
            showExplanatoryDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showExplanatoryDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText("Update class venue");
        sweetAlertDialog.setContentText("Change class venue automatically by picking a place on a map?");
        sweetAlertDialog.setConfirmText(getString(android.R.string.ok));
        sweetAlertDialog.setCancelText(getString(android.R.string.cancel));
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            initTestLocation();
            sweetAlertDialog1.dismissWithAnimation();
        });
        sweetAlertDialog.setCancelClickListener(SweetAlertDialog::dismissWithAnimation);
        sweetAlertDialog.show();
    }

    private void initTestLocation() {
        try {
            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            startActivityForResult(i, Constants.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                .onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }

            showConfirmDialog(place);

        }else{
            super.onActivityResult(requestCode, resultCode, data);
            getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                    .onActivityResult(requestCode, resultCode, data);

        }
    }

    private void showConfirmDialog(Place place) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText("Update class venue");
        sweetAlertDialog.setContentText("Change class venue to location " + place.getName());
        sweetAlertDialog.setConfirmText(getString(android.R.string.ok));
        sweetAlertDialog.setCancelText(getString(android.R.string.cancel));
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            updateLocation(place);
            sweetAlertDialog1.dismissWithAnimation();
        });
        sweetAlertDialog.setCancelClickListener(SweetAlertDialog::dismissWithAnimation);
        sweetAlertDialog.show();
    }

    private void updateLocation(Place place) {
        LatLng latLng = place.getLatLng();
        attendance.getVenue().setLatitude(latLng.latitude);
        attendance.getVenue().setLongitude(latLng.longitude);
        attendance.getVenue().setRadius(10000);
        geoLocationFragment.setAttendance(attendance);
    }

}
