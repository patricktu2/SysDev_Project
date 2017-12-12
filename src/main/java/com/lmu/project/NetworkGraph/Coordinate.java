/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.NetworkGraph;

/**
 *
 * @author Patrick
 */
public class Coordinate {
    double longitude;
    double latitude;
    
    
    public String toString(){
        return "(" + this.getLongitude() + "," + this.getLatitude() +")";
    }
    
    public Coordinate() {
    }

    public Coordinate(double lon, double lat) {
        this.longitude = lon;
        this.latitude  = lat;
    }
    
    
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    
}
