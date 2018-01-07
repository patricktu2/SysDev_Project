/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmu.project.NetworkGraph.DijkstraAlgorithm;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * 
 * @author Patrick
 */
@Path("google_direction")
public class GoogleDirections {
    
    private static final String GOOGLE_DIRECTIONS = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_KEY = "key=AIzaSyDoj0ftlp61_llX9mR2IKuBGCubR-JjWWA";
    

    
    /**
     * Performs a Google Direction API Call based on 4 Parameters (latitude and longitude for origin and destination) 
     * given via the URL and returns a GEO JSON
     * @param origin_lat
     * @param origin_lon
     * @param dest_lat
     * @param dest_lon
     * @return 
     */
    @GET
    @Path("/URI")
    @Produces(MediaType.APPLICATION_JSON)
    public static String waypoinSearchJersey_uri(
            @QueryParam("originLat") double origin_lat,
            @QueryParam("originLon") double origin_lon, 
            @QueryParam("destinationLat") double dest_lat,
            @QueryParam("destinationLon") double dest_lon) {
        //Sent information to google and wait

        System.out.println("Call 'Google Path (via Server [URI])' via http://localhost:9090/sysdev/google_direction/uri");
        System.out.println("PARAMETERS: OriginLat="+origin_lat+" OriginLon="+origin_lon + " DestLat="+dest_lat + " DestLon="+ dest_lon);
        
        Client client = Client.create();
        WebResource webResource = client.resource(buildWaypointQueryString(origin_lat, origin_lon, dest_lat, dest_lon));
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());
        }
  
        String responseString = response.getEntity(String.class);
        //System.out.println(responseString);
        return responseString;
    }
    
    /**
     * Performs a Google Direction API Call based on a JSON file where the lat an lon coordinates 
     * are extracted and returns a GEO JSON
     * @param json_string
     * @return 
     */
    @POST
    @Path("/obj")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public static String waypoinSearchJersey_obj(String json_string) {
        //Sent information to google and wait

        System.out.println("Call 'Google Path (via Server [OBJ])' via http://localhost:9090/sysdev/google_direction/obj");
        System.out.println("Received via HTTP POST:\n"+json_string);
        
        // Create new Object Mapper for JSON parsing
        ObjectMapper mapper = new ObjectMapper();
        
        RequestObject ro = null;
        double origin_lat = 0;
        double origin_lon = 0;
        double dest_lat = 0;
        double dest_lon = 0;
        
        try {
            ro = mapper.readValue(json_string, RequestObject.class);
            origin_lat = ro.getS().getLat();
            origin_lon = ro.getS().getLon();
            dest_lat = ro.getT().getLat();
            dest_lon = ro.getT().getLon();
            
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(ro);
        
        Client client = Client.create();
        WebResource webResource = client.resource(buildWaypointQueryString(origin_lat, origin_lon, dest_lat, dest_lon));
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());
        }
        // Response String seems to be null
        String responseString = response.getEntity(String.class);
        //System.out.println(responseString);
        return responseString;
    } 
    
    /**
     * Performs encodes the coordinates of the JSON parameter and calls the Dijkstra Algorithm
     * to perform a shortest path search
     * @param json_string
     * @return 
     */
    @POST
    @Path("/dijkstra")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static String dijkstraSearch (String json_string){
        System.out.println("Call Dijkastra Routing Algorithm via http://localhost:9090/sysdev/google_direction/dijkstra");
        //System.out.println("Received via HTTP POST:\n"+json_string);
        
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
        String return_geo_json = DijkstraAlgorithm.performShortestPathSearch(origin_lat_rounded, origin_lon_rounded, dest_lat_rounded, dest_lon_rounded);
               
        // TEST CALL String return_geo_json = DijkstraAlgorithm.performShortestPathSearch(54.4606423, 9.827986, 54.4608511, 9.8290738);
        
        return return_geo_json;
    }
    
    /**
     * Builds a query string with given latitude and longitude coordinates as attributes in 
     * the URL for a google direction API call
     * @param origin_lat
     * @param origin_lon
     * @param dest_lat
     * @param dest_lon
     * @return 
     */
    public static String buildWaypointQueryString(double origin_lat, double origin_lon, double dest_lat, double dest_lon){
        String origin = "origin=" + origin_lat + "," + origin_lon;
        String destination = "destination=" + dest_lat + "," + dest_lon;
        return GOOGLE_DIRECTIONS + origin + "&" + destination + "&" + GOOGLE_KEY;
    }
    
    
}
