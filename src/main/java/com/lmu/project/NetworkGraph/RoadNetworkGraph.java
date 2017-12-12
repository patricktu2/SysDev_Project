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

    public RoadNetworkGraph() {

    }
    

    
    
}
