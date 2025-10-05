package com.example.lab4;

public class LocationResponse {
    private int id;
    private String name;
    private String region;
    private String country;
    private double lat;
    private double lon;
    private String url;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getRegion() { return region; }
    public String getCountry() { return country; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public String getUrl() { return url; }

    @Override
    public String toString() {
        return name + " - " + region + ", " + country + "\n(" + lat + ", " + lon + ")";
    }
}
