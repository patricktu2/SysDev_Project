/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.NetworkGraph;
import java.util.*;

/**
 *
 * @author Patrick
 */
public class Vertex {
    Coordinate coordinate;
    List <Edge> outgoingEdges = new ArrayList <> (); //(Assume bidirectional edges) Outgoing Edge List to be added
    List <Vertex> neighbours = new ArrayList <> ();
    
    
    // Method addEdges  
    // Method List<Vertex> getNeighbourNode
    // Method getDistance; if not connected set to infinity
    
    public Vertex(double lon, double lat) {
        this.coordinate = new Coordinate(lon,lat);
    }
    
    public Vertex (Coordinate coordinate){
        this.coordinate = coordinate;
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
