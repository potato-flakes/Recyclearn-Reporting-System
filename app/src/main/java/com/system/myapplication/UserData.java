package com.system.myapplication;

public class UserData {
    private String crimeType;
    private String crimeDetails;
    private String personalDetails;
    private String summaryDetails;

    // Create getters and setters for each field

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getCrimeDetails() {
        return crimeDetails;
    }

    public void setCrimeDetails(String crimeDetails) {
        this.crimeDetails = crimeDetails;
    }

    public String getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(String personalDetails) {
        this.personalDetails = personalDetails;
    }

    public String getSummaryDetails() {
        return summaryDetails;
    }

    public void setSummaryDetails(String summaryDetails) {
        this.summaryDetails = summaryDetails;
    }
}
