package com.system.myapplication;

import org.osmdroid.util.GeoPoint;

public interface LocationSelectionListener {
    void onLocationSelected(double retrievedLatitude, double retrievedLongitude);
}


