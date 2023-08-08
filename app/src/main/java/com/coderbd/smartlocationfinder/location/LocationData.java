package com.coderbd.smartlocationfinder.location;

public class LocationData {
    private int id;
    private String cname;
    private String name;
    private String lat;
    private String lon;

    public LocationData(int id) {
        this.id = id;
    }

    public LocationData(int id, String cname, String name, String lat, String lon) {
        this.id = id;
        this.cname = cname;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public LocationData(String cname, String name, String lat, String lon) {
        this.cname = cname;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
