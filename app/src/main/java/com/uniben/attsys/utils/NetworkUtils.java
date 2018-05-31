package com.uniben.attsys.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Cyberman on 3/31/2018.
 */

public class NetworkUtils {
    public static boolean userDeviceOnline(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
