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
    
    
    
    public Vertex(double lat, double lon) {
        this.coordinate = new Coordinate(lat,lon);
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
    
    
    /**
     * Overwrite for the contain method to work. Equal if two vertexes have the same lat and lon coodinate values.
     * @param o
     * @return 
     */
    @Override
    public boolean equals (Object o){
        boolean isEqual = false;
        
        if (o instanceof Vertex){
            if (this.coordinate.equals(((Vertex) o).coordinate )){
                isEqual = true;
            }
        }
        
        return isEqual;
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.coordinate);
        return hash;
    }
    
    @Override
    public String toString(){
        return "( " + this.coordinate.getLatitude() + ", " + this.coordinate.longitude + " )" ;
    }
    
}
