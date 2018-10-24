/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.JerseyServer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author Patrick
 */
public class RequestObject {
    
    //Inner class Marker
    public static class Marker{
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public Marker(double lat, double lon) {
            this.setLat(lat);
            this.setLon(lon);
        }
        
        public Marker(){
        }
    }
    
    
    private Marker s;
    private Marker t;

    public Marker getS() {
        return s;
    }

    public void setS(Marker s) {
        this.s = s;
    }

    public Marker getT() {
        return t;
    }

    public void setT(Marker t) {
        this.t = t;
    }
    
    public RequestObject(){
        
    }
    
    public RequestObject(double start_lat, double start_lon, double dest_lat, double dest_lon) {
        this.setS(new Marker(start_lat,start_lon));
        this.setT(new Marker(dest_lat,dest_lon));
    }

    
    public String toString (){
        return "{Start:"+ "{Lat="+this.getS().getLat()+",lon=" + this.getS().getLon() 
                + "}, Dest={"+  "Lat="+this.getT().getLat()+",lon=" + this.getT().getLon()+"}}";
    }
    
    
}
