package com.uniben.attsys.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Cyberman on 5/17/2018.
 */

public class NotificationUtils {
    public static void notifyUser(View view, String message){
        Snackbar.make(view, message,Snackbar.LENGTH_SHORT).show();
    }

    public static void notifyUser(Context context,  String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
