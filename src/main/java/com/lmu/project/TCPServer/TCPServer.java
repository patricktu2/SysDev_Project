/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.TCPServer;

import com.lmu.project.NetworkGraph.*;
import java.io.IOException;

/**
 *
 * @author schubert
 */
public class TCPServer {
    //Some Data replacing the the GeoJson File
    public static final String jsonResult = "{\"data\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"costs\":{\"Travel_Time\":1000.0,\"Distance\":100.0},\"instructions\":[]},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[13.382720947,52.536064212],[13.403406143,52.53376702]]}}]}}\n";
    public static RoadNetworkGraph graph_global;
    
    
    //instance data which might be the road network 
    public String instanceData;
    public int port = 9595;
    
    //Listener Thread for the Server
    Listener listener;    
    
    //constructor
    public TCPServer(){
        //read instance Data
        instanceData = jsonResult;       
        graph_global = NetworkInstantiation.createGraph();
        System.out.println("[TCP Server] Networkgraph loaded and TCP server instantiated");
    }
    
    /***
     * Starts the listener thread
     */    
    private void startListener() {
       listener = new Listener(port, instanceData);
       listener.start();
    }

    /***
     * Shutdown the listener
     */
    
    private void shutdown() {       
        listener.shutdown();                
    }
    
    /***
     * Start a server and shut it down when enter is pressed.
     * @param args
     * @throws IOException 
     */
     
    public static void main(String[] args) throws IOException{
        TCPServer server = new TCPServer();
        server.startListener();
        System.out.println("[TCP Server] SysDev TCP Server started on localhost:" +server.port);
        System.out.println("[TCP Server] Press any key to stop the server...");
        System.in.read();
        server.shutdown();
        System.out.println("[TCP Server] TCP Server shut down" );
        System.exit(0);
    }

    
}
