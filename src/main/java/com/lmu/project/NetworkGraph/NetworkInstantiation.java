/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.NetworkGraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geojson.*;


/**
 *
 * @author Patrick
 */
public class NetworkInstantiation {
    
    /**
     *
     * @param args
     */
    public static void main (String [] args){
          
        // --------------------JSON IMPORT ----------------------
        
        String workingDirectory = System.getProperty("user.dir");
        //System.out.println(workingDirectory);
        File json_file = new File(workingDirectory+"/schleswig-holzstein.json");
        
        FeatureCollection featureCollection = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            featureCollection = new ObjectMapper().readValue(json_file, FeatureCollection.class);
        } catch (IOException ex) {
            Logger.getLogger(NetworkInstantiation.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        // ---------------- Instantiate Nodex/Vertexes -------------------
        RoadNetworkGraph graph = new RoadNetworkGraph();
        
        //Get the first feature of the JSON file which are the nodes
        Geometry geo_nodes = (Geometry)featureCollection.getFeatures().get(0).getGeometry();
        List <LngLatAlt> nodes = geo_nodes.getCoordinates();
        
        double temp_lat;
        double temp_lon;
        
        for(LngLatAlt n: nodes ){
            temp_lat = n.getLatitude();
            temp_lon = n.getLongitude();
            Vertex vertex = new Vertex(temp_lon, temp_lat);
            //System.out.println(vertex);
            graph.vertex.add(vertex);
            
        }
                
        // ---------------- Instantiate Edges -------------------
        
        int size_of_features = featureCollection.getFeatures().size();
        for (int i = 2; i<size_of_features ; i++){
            //Create new Edge Object
            Edge edge = new Edge();
            
            //Get current edge from the JSON imported logic
            Feature f = featureCollection.getFeatures().get(i);
            Geometry geo_vertexes = (Geometry)f.getGeometry();
            
            // Get cordinates wich linkes two nodes inlcuding them
            List <LngLatAlt> edge_cordinates = geo_vertexes.getCoordinates();
            
            // Assign Maximum speed and coordinates to Edge Object
            edge.setMaxspeed((int)f.getProperties().get("maxspeed"));

            for (LngLatAlt c: edge_cordinates){
                edge.coordinates.add(new Coordinate(c.getLongitude(),c.getLatitude()));
            }
            //System.out.println(edge);
        }
    }
    
}
