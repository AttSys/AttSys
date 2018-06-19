package com.uniben.attsys.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.uniben.attsys.R;

import cn.pedant.SweetAlert.SweetAlertDialog;



/**
 * Created by Cyberman on 11/12/2017.
 */

public class PermissionHelpers {
    private static final String TAG = "Helpers";
    public static final int REQUEST_PERMISSION_CODE1 = 101;
    public static final int REQUEST_PERMISSION_CODE2 = 102;
    public static final int REQUEST_PERMISSION_CODE3 = 103;

    public static boolean isWriteExternalStoragePermissionGranted(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.i(TAG, "checkPermission: ");
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCameraPermissionGranted(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        Log.i(TAG, "checkPermission: ");
        return result == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestCameraPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA )) {
            Log.i(TAG, "requestPermission: IF");
            buildCameraExplanatoryDialog(activity);
        } else {
            Log.i(TAG, "requestPermission: ELSE");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE2);
        }
    }

    public static void requestWriteExternalStoragePermission1(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE )) {
            Log.i(TAG, "requestPermission: IF");
            buildWriteExternalStorageExplanatoryDialog(activity);
        } else {
            Log.i(TAG, "requestPermission: ELSE");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE1);
        }
    }




    private static void buildWriteExternalStorageExplanatoryDialog(final Activity activity){
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(activity.getString(R.string.allow_write_permission))
                .setContentText(activity.getString(R.string.allow_write_permission_message))
                .setConfirmText(activity.getString(android.R.string.ok))
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();

                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE3);

                })
                .show();
    }

    private static void buildCameraExplanatoryDialog(final Activity activity){
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(activity.getString(R.string.allow_camera_permission))
                .setContentText(activity.getString(R.string.allow_camera_permission_message))
                .setConfirmText(activity.getString(android.R.string.ok))
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();

                    requestCameraPermission(activity);

                })
                .show();
    }



}
