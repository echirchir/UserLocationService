package com.simpledeveloper.geotagme;


public class UserLocationAcquiredEvent {

    private boolean isCompleted;

    private double latitude;

    private double longitude;

    UserLocationAcquiredEvent(boolean isCompleted, double latitude, double longitude) {
        this.isCompleted = isCompleted;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
