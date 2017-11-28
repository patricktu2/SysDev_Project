/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Patrick
 */
public class GoogleDirections {
    
    private static final String GOOGLE_DIRECTIONS = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_KEY = "key=AIzaSyDoj0ftlp61_llX9mR2IKuBGCubR-JjWWA";
    
    public static void waypoinSearchJersey(double origin_lat, double origin_lon, double dest_lat, double dest_lon) {
        //Sent information to google and wait
        Client client = Client.create();
        WebResource webResource = client.resource(buildWaypointQueryString(origin_lat, origin_lon, dest_lat, dest_lon));
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());
        }
        String responseString = response.getEntity(String.class);
        System.out.println(responseString);
    }
    
        
    public static String buildWaypointQueryString(double origin_lat, double origin_lon, double dest_lat, double dest_lon){
        String origin = "origin=" + origin_lat + "," + origin_lon;
        String destination = "destination=" + dest_lat + "," + dest_lon;
        return GOOGLE_DIRECTIONS + origin + "&" + destination + "&" + GOOGLE_KEY;
    }
    
    
}
