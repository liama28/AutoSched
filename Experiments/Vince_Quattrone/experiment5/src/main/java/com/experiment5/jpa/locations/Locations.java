package com.experiment5.jpa.locations;


import javax.persistence.*;

@Entity
public class Locations {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="lon")
    private double lon;
    @Column(name="lat")
    private double lat;
    @Column(name="name")
    private String name;

    public Locations(){

    }

    public Locations(double lon, double lat, String name){
        this.lon = lon;
        this.lat = lat;
        this.name = name;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public double getLon() {return lon;}

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {return lat;}

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

}
