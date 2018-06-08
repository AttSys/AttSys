
package com.uniben.attsys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.uniben.attsys.fragments.GeoLocationFragment;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.utils.ActivityUtils;
import com.uniben.attsys.utils.Constants;

public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = AttendanceActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public double LAT = 6.401860;
    public double LNG = 5.617089;


    public static final String CURRENT_LOCATION = "6.4021796, 5.6186157 ";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        GeoLocationFragment geoLocationFragment = (GeoLocationFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (geoLocationFragment == null) {
            if (getIntent().hasExtra(Constants.ATTENDANCE_KEY)){
                Attendance attendance = getIntent().getParcelableExtra(Constants.ATTENDANCE_KEY);
                attendance.getVenue().setLatitude(LAT);
                attendance.getVenue().setLongitude(LNG);
                attendance.getVenue().setRadius(1000);
                geoLocationFragment =  GeoLocationFragment.newInstance(attendance);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        geoLocationFragment, R.id.fragment_container);

            }
        }



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
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
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                .onActivityResult(requestCode, resultCode, data);
    }
}
