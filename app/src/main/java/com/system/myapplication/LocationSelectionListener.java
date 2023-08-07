package com.system.myapplication;

public interface LocationSelectionListener {
    void handleLocation(double passLatitude, double passLongitude);
    boolean isLocationSet();
}
