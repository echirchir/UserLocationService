package com.simpledeveloper.geotagme;

import io.realm.RealmObject;

public class UserLocation extends RealmObject {

    private int id;
    private double latitude;
    private double longitude;
    private String createdAt;

    public UserLocation(){}

    public UserLocation(int id, double latitude, double longitude, String createdAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
