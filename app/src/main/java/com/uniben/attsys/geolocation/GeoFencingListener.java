package com.uniben.attsys.geolocation;

public interface GeoFencingListener {
    void onRequestPermission();
    void onUpdateGeoFence(boolean added);

    void onError(String message);
}
