/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Patrick
 */

/* Root resource (exposed at "myresource" path) */
@Path("myresource")
public class MyResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        double ori_lat= 52.533975860483864;       
        double ori_lon = 13.163554809570312;
        double dest_lat= 52.633975860483864;
        double dest_lon = 24.565554809570312;
        
        String responseString = GoogleDirections.waypoinSearchJersey_uri(ori_lat, ori_lon, dest_lat, dest_lon);
        //System.out.println(responseString)
        
        return responseString;
    }
    
}
