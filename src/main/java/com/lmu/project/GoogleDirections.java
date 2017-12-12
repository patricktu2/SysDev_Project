/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
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
     * Via URL call takes 4 parameters (latitude and longitude for origin and destination)
     * Performs Google Directions API Call
     * returns JSON file
     * 
     * Test URL: http://localhost:9090/sysdev/google_direction?originLat=52.533975860483864+&originLon=13.163554809570312+&destinationLat=52.633975860483864&destinationLon=24.565554809570312
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
        System.out.println(responseString);
        return responseString;
    }
    

    @POST
    @Path("/obj")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    //@Consumes(MediaType.TEXT_PLAIN)

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
        System.out.println(responseString);
        return responseString;
    } 
    
    
    
    
    public static String buildWaypointQueryString(double origin_lat, double origin_lon, double dest_lat, double dest_lon){
        String origin = "origin=" + origin_lat + "," + origin_lon;
        String destination = "destination=" + dest_lat + "," + dest_lon;
        return GOOGLE_DIRECTIONS + origin + "&" + destination + "&" + GOOGLE_KEY;
    }
    
    
}
