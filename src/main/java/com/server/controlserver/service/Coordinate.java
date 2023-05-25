package com.server.controlserver.service;

public class Coordinate {
    private double latitude; // 위도
    private double longitude; // 경도

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
