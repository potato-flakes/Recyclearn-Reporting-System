package com.system.myapplication;

public class UserData {
    private String crimeType;
    private boolean isYesButtonSelected;
    private String crimePerson;
    private String crimeDate;
    private String crimeTime;
    private int crimeHour = -1;
    private int crimeMinute = -1;
    private String crimeLocation;
    private String crimeTimeIndication;

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
    public String getCrimeLocation() {
        return crimeLocation;
    }

    public void setCrimeLocation(String crimeLocation) {
        this.crimeLocation = crimeLocation;
    }
    public String getCrimeDescription() {
        return crimeLocation;
    }

    public void setCrimeDescription(String crimeLocation) {
        this.crimeLocation = crimeLocation;
    }
}
