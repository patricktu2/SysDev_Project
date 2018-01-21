/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.TCPServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmu.project.JerseyServer.RequestObject;
import com.lmu.project.JerseyServer.TestClient;
import com.lmu.project.NetworkGraph.DijkstraAlgorithm;
import static com.lmu.project.NetworkGraph.DijkstraAlgorithm.findClosestVertex;
import com.lmu.project.NetworkGraph.RoadNetworkGraph;
import com.lmu.project.NetworkGraph.Vertex;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Includes a method to start disjktra search
 * @author Patrick
 */
public class DijkstraHandler {
    
    private static long searchID = 0;
    
    /**
     * Handles Mapping of JSON file, extracts cooordinates and passes them on to perform Dijkstra Search
     * @param json_string
     * @return 
     */
    public String startDijkstraSearch(String json_string){
        searchID++;
        System.out.println("[DijkstraHandler #"+searchID+"]: Call Dijkstra Search and perform it");
        //System.out.println("Received via HTTP POST:\n"+json_string);
        
        //System.out.println("startDijkstraSearch: Input -"+ json_string);
        
        // Create new Object Mapper for JSON parsing
        ObjectMapper mapper = new ObjectMapper();
        
        RequestObject ro = null;
        Double origin_lat = 0.0;
        Double origin_lon = 0.0;
        Double dest_lat = 0.0;
        Double dest_lon = 0.0;
        
        try {
            ro = mapper.readValue(json_string, RequestObject.class);
            origin_lat = ro.getS().getLat();
            origin_lon = ro.getS().getLon();
            dest_lat = ro.getT().getLat();
            dest_lon = ro.getT().getLon();
            
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Rounding precision to 6 decimal digits as this is the precision in the JSON File
        DecimalFormat df = new DecimalFormat("#.######"); 
        df.setRoundingMode(RoundingMode.FLOOR); //Optional
        
        Double origin_lat_rounded = Double.parseDouble(df.format(origin_lat) );
        Double origin_lon_rounded = Double.parseDouble(df.format(origin_lon) );
        Double dest_lat_rounded = Double.parseDouble(df.format(dest_lat) );
        Double dest_lon_rounded = Double.parseDouble(df.format(dest_lon) );
                     
        //Call Dijkstra Algorithm and pass coordinates
        String return_geo_json = this.performShortestPathSearch(origin_lat_rounded, origin_lon_rounded, dest_lat_rounded, dest_lon_rounded);
        
        //System.out.println("startDijkstraSearch: "+return_geo_json);
        
        // TEST CALL String return_geo_json = DijkstraAlgorithm.performShortestPathSearch(54.4606423, 9.827986, 54.4608511, 9.8290738);
        
        return return_geo_json;
        
    }
    
    
    /**
     * Takes global graph saved in Server and use for Dijkstra Search
     * @param origin_lat
     * @param origin_lon
     * @param dest_lat
     * @param dest_lon
     * @return 
     */
    private String performShortestPathSearch(double origin_lat,double origin_lon, double dest_lat, double dest_lon){
        //Fetch parameters and create vertex object
        Vertex origin = new Vertex(origin_lon,origin_lat);
        Vertex destination = new Vertex (dest_lon, dest_lat);            
        
        //Graph as global variable so that it needs to be instantiated just once
        RoadNetworkGraph graph = TCPServer.graph_global;
        
        //If the coordinate cannot be found within the graph get the closest vertex as an approximate
        origin = findClosestVertex(origin,graph);
        destination = findClosestVertex(destination,graph);
        
        //Perform Dijkstra Algorithm; Return Object with attribute "Distance" and "Path"

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        
        double startTime = System.currentTimeMillis();
        HashMap <String, Object > returnObject = dijkstra.execute(origin,destination);
        double stopTime = System.currentTimeMillis();
        
        DecimalFormat df_distance = new DecimalFormat("#.###"); 
        String distance = df_distance.format(returnObject.get("distance"));
        DecimalFormat df_time = new DecimalFormat("#.##");
        String time = df_time.format(returnObject.get("time"));
        
        
        //System.out.println("Distance in KM ="+ distance);
        //System.out.println("Path=" + returnObject.get("path"));
        //System.out.println("Travel Time in Minutes =" + time);
        
        // Construct JSON response in the needed form
        String json_part1 = "{\n" +
                            "	\"type\":\"FeatureCollection\",\n" +
                            "	\"features\":[{\n" +
                            "		\"type\":\"Feature\",\n" +
                            "		\"geometry\":\n" +
                            "			{\"type\":\"LineString\",\"coordinates\":";
        String path = returnObject.get("path").toString();
        String json_part2 = "},\n" +
                            "           \"properties\":{\"costs\":{\"Distance\":";
        String json_part3 = ",\"Travel_Time\": \" "+ time + "\"},\n" +
                            "		\"instructions\":[]}\n" +
                            "		}]\n" +
                            "	}";
        
        String json_string = json_part1 + path + json_part2 + distance + json_part3;
        
        //System.out.println(json_string);
        
        return json_string;
        
    }
    
}
