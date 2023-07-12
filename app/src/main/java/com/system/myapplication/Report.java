package com.system.myapplication;

// Report.java

import java.util.ArrayList;
import java.util.List;

public class Report {
    private String reportId;
    private String description;
    private String location;
    private String date;
    private String time;
    private List<String> imageUrls;
    private List<String> imagePaths; // Ad

    public Report(String reportId, String description, String location, String date, String time) {
        this.reportId = reportId;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.imageUrls = new ArrayList<>();
        imagePaths = new ArrayList<>();
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }
    public void setDateTime(String dateTime) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void addImageUrl(String imageUrl) {
        imageUrls.add(imageUrl);
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void addImagePath(String imageUrl) {
        // Add the image URL to the imagePaths list
        imagePaths.add(imageUrl);
    }

    public List<String> getImagePaths() {
        // Return the list of image paths
        return imagePaths;
    }
}
