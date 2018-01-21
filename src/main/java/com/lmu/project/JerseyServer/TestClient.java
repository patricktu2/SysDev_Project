/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.JerseyServer;

import com.lmu.project.JerseyServer.RequestObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Patrick
 */
public class TestClient {
    
    public static void main (String [] args){
        
                // Test Call with google directions
        double ori_lat= 52.533975860483864;       
        double ori_lon = 13.163554809570312;
        double dest_lat= 52.633975860483864;
        double dest_lon = 24.565554809570312;
        
        RequestObject test = new RequestObject(ori_lat,ori_lon, dest_lat, dest_lon );
        System.out.println(test);
        
        String json = "{\"s\":{\"lat\":52.43759500093112,\"lon\":13.65325927734375},\"t\":{\"lat\":52.505773395615016,\"lon\":13.36212158203125}}";
        
        ObjectMapper mapper = new ObjectMapper();
        
        
        try {
            RequestObject ro = mapper.readValue(json, RequestObject.class);
            System.out.println(ro);
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        

    }
    
}
