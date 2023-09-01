package com.system.myapplication;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private String crimeType;
    private boolean isYesButtonSelected = true;
    private boolean locationEnabled;
    private String crimePerson;
    private String crimeDate;
    private String crimeTime;
    private String selectedBarangay;
    private int crimeHour = -1;
    private int crimeMinute = -1;
    private String crimeLocation;
    private String crimeTimeIndication;
    private double latitude;
    private double longitude;
    private String crimeDescription;
    private String userFirstName;
    private String userLastName;
    private String userSex;
    private String userPhone;
    private String userEmail;
    private List<String> selectedImageUrls = new ArrayList<>();

    public List<String> getSelectedImageUrls() {
        return selectedImageUrls;
    }

    public void setSelectedImageUrls(List<String> selectedImageUrls) {
        this.selectedImageUrls = selectedImageUrls;
    }

    // Create getters and setters for each field

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getCrimePerson() {
        return crimePerson;
    }

    public void setCrimePerson(String crimePerson) {
        this.crimePerson = crimePerson;
    }
    public boolean isYesButtonSelected() {
        return isYesButtonSelected;
    }

    public void setYesButtonSelected(boolean yesButtonSelected) {
        isYesButtonSelected = yesButtonSelected;
    }

    public String getCrimeDate() {
        return crimeDate;
    }

    public void setCrimeDate(String crimeDate) {
        this.crimeDate = crimeDate;
    }

    public String getCrimeTime() {
        return crimeTime;
    }

    public void setCrimeTime(String crimeTime) {
        this.crimeTime = crimeTime;
    }

    public int getCrimeHour() {
        return crimeHour;
    }

    public void setCrimeHour(int crimeHour) {
        this.crimeHour = crimeHour;
    }
    public int getCrimeMinute() {
        return crimeMinute;
    }

    public void setCrimeMinute(int crimeMinute) {
        this.crimeMinute = crimeMinute;
    }
    public String getCrimeTimeIndication() {
        return crimeTimeIndication;
    }

    public void setCrimeTimeIndication(String crimeTimeIndication) {
        this.crimeTimeIndication = crimeTimeIndication;
    }
    public String getCrimeExactLocation() {
        return crimeLocation;
    }

    public void setCrimeExactLocation(String crimeLocation) {
        this.crimeLocation = crimeLocation;
    }
    public String getSelectedBarangay() {
        return selectedBarangay;
    }

    public void setSelectedBarangay(String selectedBarangay) {

        this.selectedBarangay = selectedBarangay;
    }
    public boolean isLocationEnabled() {
        return locationEnabled;
    }

    public void setLocationEnabled(boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
    }

    public double getCrimeLatitude() {
        return latitude;
    }
    public void setCrimeLatitude(Double latitude) {

        this.latitude = latitude;
    }

    public double getCrimeLongitude() {
        return longitude;
    }
    public void setCrimeLongitude(Double longitude) {

        this.longitude = longitude;
    }
    public String getCrimeDescription() {
        return crimeDescription;
    }

    public void setCrimeDescription(String crimeDescription) {
        this.crimeDescription = crimeDescription;
    }
    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }
    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
