/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.NetworkGraph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.*;

/**
 *
 * @author Patrick
 */
@JsonIgnoreProperties({ "type" })
public class RoadNetworkGraph {
    List <Vertex> vertex = new ArrayList <> ();
    List <Edge> edges = new ArrayList <>();
    
    // Method to add Intermediate Nodes/edges to the Node List (from the edges)
    
    //Dijkstra: distance map (recent distance from start to node i ) as hasmap; previousNode as Hashmap
    // run: list of processed nodes, go to shortest distance node, traverse to all neighbourhood nodes
    // Complexity is O(n^)
    
    /**
     * A* algorithm
     * Optimization function f= distance + estimation/heuristics (heuristic can be e.g., euklidian distance)
     */
    
    public RoadNetworkGraph() {

    }
    
    public String getStructure (){
        return "GRAPH STRUCTURE | Vertexes:"+this.vertex.size()  + " - Edges:" + this.edges.size();
    }
    
    
}
