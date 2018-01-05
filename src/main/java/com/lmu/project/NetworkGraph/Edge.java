/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.NetworkGraph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patrick
 * 
 * Represents an undirected edge that connects two nodes/vertexes
 * and has a maximum speed limit
 */
public class Edge {

    Coordinate origin;
    Coordinate destination;
    int maxspeed;
    private Double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    /**
     * Compute the distance of the origin and destination coordinates and assignes it to the distance attribute
     */
    public void assignDistance(){
        this.distance = Coordinate.computeDistance(this.origin, this.destination);
    }
    
    public Edge() {
    }

    public String toString(){
        return "{ "+origin +";" + destination + "; Max speed="+ maxspeed + "; Distance="+ this.getDistance() +"}";
    }
    
    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    public int getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(int maxspeed) {
        this.maxspeed = maxspeed;
    }
}
