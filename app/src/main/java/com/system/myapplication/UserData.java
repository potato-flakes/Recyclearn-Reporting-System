package com.system.myapplication;

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
    public String getCrimeDescription() {
        return crimeLocation;
    }

    public void setCrimeDescription(String crimeLocation) {
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
}
