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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geojson.*;


/**
 *Collection of methods to fully create a RoadNetworkGraph
 * 
 * @author Patrick
 */
public class NetworkInstantiation {
    
 
    /**
     * 
     * Traverses a graph and checks if a coordinate is contained already in the graph.
     * Returns true if coordinate is a duplicate.
     * 
     * @param c
     * @param graph
     * @return 
     */
    public static boolean vertexInGraph (Coordinate c, RoadNetworkGraph graph){
        boolean vertexInGraph = false;
        for (Vertex v: graph.vertex){
            if(v.getCoordinate().longitude == c.longitude && v.getCoordinate().latitude == c.latitude){
                vertexInGraph = true;
                break;
            }
                
        }
        return vertexInGraph;
    }
    
    /**
     * Instantiates and creates a graph object of the class RoadNetworkGraph
     * based on a GEO Json file in the directory and adds all associating nodes and edges to the graph
     * @return 
     */
    public static RoadNetworkGraph createGraph (){   
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
        
        // --------- Instantiate inital Vertexes that are in the Multiploint Objects of the JSON File ------------
        RoadNetworkGraph graph = new RoadNetworkGraph();
        
        //Get the first feature of the JSON file which are the nodes
        Geometry geo_nodes = (Geometry)featureCollection.getFeatures().get(0).getGeometry();
        List <LngLatAlt> nodes = geo_nodes.getCoordinates();
        
        double temp_lat;
        double temp_lon;
        
        for(LngLatAlt n: nodes ){
            temp_lat = n.getLatitude();
            temp_lon = n.getLongitude();
            Vertex vertex = new Vertex(temp_lat, temp_lon);
            //System.out.println(vertex);
            graph.vertex.add(vertex);
            
        }              
        // ---------------- Instantiate Edges and add missing "lonely nodes" -------------------     
        int size_of_features = featureCollection.getFeatures().size();
        // Second element onwards in the JSON file are the edges represented as list of coordinate pairs
        for (int i = 1; i<size_of_features ; i++){
            
            //Get current edge from the JSON imported logic
            Feature f = featureCollection.getFeatures().get(i);
            Geometry geo_vertexes = (Geometry)f.getGeometry();
            
            // Get cordinates wich linkes two nodes inlcuding destination, origin and "lonely nodes"
            List <LngLatAlt> edge_cordinates = geo_vertexes.getCoordinates();
        
            Iterator <LngLatAlt> it = edge_cordinates.iterator();
            LngLatAlt first_element = it.next();
           
            // Traversing through each element of Coordinaes in the list and furthermore
            // a) Adds every consecutive pair of coordinates as an edge to the edge list of the graph
            // b) if an a coordinate is a "lonely" node and not contained in the graph, add it to the vertex list
            while (it.hasNext()){
                LngLatAlt second_element = it.next();
                
                //Create two temporary vertexes 
                Coordinate origin = new Coordinate (first_element.getLongitude(), first_element.getLatitude());
                Coordinate destination = new Coordinate (second_element.getLongitude(), second_element.getLatitude());
                
                // Check if nodes are already in the Node list of the graph or lonely nodes
                // If not add to the vertex list
                if (! vertexInGraph(origin, graph)){
                    Vertex temp_ver = new Vertex(origin);
                    graph.vertex.add(temp_ver);
                }
                if (! vertexInGraph(destination, graph)){
                    Vertex temp_ver = new Vertex(destination);
                    graph.vertex.add(temp_ver);
                }
                
                //Create new Edge Object
                Edge edge = new Edge();
                // Assign Maximum speed, distance, origin and destination to Edge Object
                edge.setMaxspeed((int)f.getProperties().get("maxspeed"));
                edge.setOrigin(origin);
                edge.setDestination(destination);
                edge.assignDistance();
                
                //Add Edge Object to list of the graph
                graph.edges.add(edge);
                    
                //Update first element for next iteration
                first_element = second_element;
            }
        }    
        return graph;
    }
    
    /**
     * Takes a graph and sets for each vertex the list of associating edges and neighbor vertexes it is directly connected to
     * @param graph 
     */
    public static void instantiateEdgesOfVertexes(RoadNetworkGraph graph){
        List <Vertex> vertexes = graph.vertex;
        List <Edge> edges = graph.edges;
        
        for (Vertex v: vertexes){
            Coordinate temp_cor = v.getCoordinate();
            
            for (Edge e: edges){
                Coordinate temp_or = e.origin;
                Coordinate temp_des = e.destination;
                
                //If one of the vertexes in the edges is the current vertex add this add to edge list and 
                // add the corresponding other vertex to the neighbour list
                if(  temp_cor.equals(temp_or) || temp_cor.equals(temp_des)){ //Note: own equals method implemented
                    v.outgoingEdges.add(e);
                    
                    if (! temp_cor.equals(temp_or)){
                        v.neighbours.add( new Vertex (temp_or) );
                    }
                    if (! temp_cor.equals(temp_des)){
                        v.neighbours.add(new Vertex (temp_des) );
                    }
                }    
            }
            
        }
    }
    
    
    
    public static void main (String [] args){
        RoadNetworkGraph graph = NetworkInstantiation.createGraph();
        
        NetworkInstantiation.instantiateEdgesOfVertexes(graph);
        System.out.println(graph.getStructure());
        /*
        System.out.println(graph.vertex.get(0).coordinate);
        System.out.println(graph.vertex.get(0).outgoingEdges);
        System.out.println(graph.vertex.get(0).neighbours);
        */
        
        Vertex v = graph.vertex.get(1769);
        Vertex z = new Vertex(new Coordinate(54.4870993,9.8603899));
        
        System.out.println(graph.edges);
        
        System.out.println("Starting vertex = " + v );
        System.out.println("Target vertex = " + z);
        System.out.println("Direct distance = " + Coordinate.computeDistance(v.getCoordinate(), z.getCoordinate()));

        
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        
        dijkstra.execute(v);
        LinkedList<Vertex> path = dijkstra.getPath(z);
        
        System.out.println("Shortest distance by dijkstra = " + dijkstra.getShortestDistance(z));
        
        System.out.println(path);
        
        
    }
    
}
