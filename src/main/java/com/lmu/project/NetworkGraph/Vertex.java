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
public class Vertex {
    Coordinate coordinate;

    public Vertex(double lon, double lat) {
        this.coordinate = new Coordinate(lon,lat);
    }

    
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    
    
    @Override
    public String toString(){
        return "{" + "lon=" + this.coordinate.getLongitude() + ", lat=" + this.coordinate.latitude + " }" ;
    }
    
}
