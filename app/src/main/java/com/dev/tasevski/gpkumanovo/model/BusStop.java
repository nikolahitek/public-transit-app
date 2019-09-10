package com.dev.tasevski.gpkumanovo.model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class BusStop implements Comparable<BusStop>{
    private LatLng location;
    private String name;
    private int id;
    private double distance;
    private boolean isRegion;

    public BusStop() {
        this.name = "NON";
        this.id = 0;
        this.location = new LatLng(0,0);
        distance = 0;
        isRegion = false;
    }

    public BusStop(String name, int id, LatLng location) {
        this.name = name;
        this.id = id;
        this.location = location;
        distance = 0;
        isRegion = false;
    }

    public BusStop(String name, int id, LatLng location, boolean isRegion) {
        this.name = name;
        this.id = id;
        this.location = location;
        distance = 0;
        this.isRegion = isRegion;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public LatLng getLocation() {
        return location;
    }

    public double getDistanceFromUser() {
        return distance;
    }

    public void setDistanceFromUser(LatLng userLastLocation) {
        distance = calculateDistance(userLastLocation, location);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double calculateDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371; //radius of Earth in km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

    @Override
    public int compareTo(@NonNull BusStop busStop) {
        return Double.compare(this.distance, busStop.distance);
    }

    public boolean isRegion() {
        return isRegion;
    }
}
