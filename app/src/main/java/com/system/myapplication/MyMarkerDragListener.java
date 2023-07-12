package com.system.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.widget.TextView;

import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;

public class MyMarkerDragListener implements Marker.OnMarkerDragListener {
    private boolean isMarkerDragEnabled = false; // Track the drag state of the marker
    private Geocoder geocoder; // Declare the Geocoder object'
    private TextView locationTextView;

    public MyMarkerDragListener(Geocoder geocoder, TextView locationTextView) {
        this.geocoder = geocoder; // Initialize the Geocoder object
        this.locationTextView = locationTextView; // Initialize the TextView object
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // Handle marker drag in progress
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // Handle marker drag end
        double latitude = marker.getPosition().getLatitude();
        double longitude = marker.getPosition().getLongitude();

        // Reverse geocode the latitude and longitude to get the location address
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                // Set the location only if it's not already set
                locationTextView.setText(address.getAddressLine(0));
                // Update the desired location with the new latitude and longitude
                // You can perform any necessary actions here, such as updating the UI or saving the location
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        // Check if dragging is enabled
        if (!isMarkerDragEnabled) {
            return;
        }
        // Handle marker drag start
    }

    public void setDragEnabled(boolean enabled) {
        isMarkerDragEnabled = enabled;
    }
}
