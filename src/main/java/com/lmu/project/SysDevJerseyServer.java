//package de.lmu.ifi.dbs.sysdev.jersey;
package com.lmu.project;

import com.lmu.project.GoogleDirections;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.util.Scanner;
import javax.ws.rs.core.MediaType;


public class SysDevJerseyServer {

    private static String BASE_URL = "http://localhost:9090/sysdev/";

    
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String uri) {
        // create a resource config that scans for JAX-RS resources and providers in de.lmu.ifi.dbs.sysdev.jersey package
        ResourceConfig rc = new ResourceConfig().packages("com.lmu.project");
        rc.register(new CORSFilter());
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer(BASE_URL);

        System.out.println(String.format("--------SysDev Jersey Server started with WADL available at %sapplication.wadl --------", BASE_URL));
        
        // Establish Client connection and open stream connection for communication
        /*
        try{
            int portNumber = 4444;
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
        }
        catch(IOException e){
            System.err.println("Couldn't get I/O for connection to: tornado");
            e.printStackTrace();
            System.exit(1);
        }
        */
        /*
        //Define a scanner
        Scanner scanner = new Scanner(System.in);
        
        // Test Call with google directions
        double ori_lat= 52.533975860483864;       
        double ori_lon = 13.365554809570312;
        double dest_lat= 67.533975860483864;
        double dest_lon = 22.365554809570312;
        
        GoogleDirections.waypoinSearchJersey(ori_lat, ori_lon, dest_lat, dest_lon);
        */
        
        //Close

        System.out.println("Press any key to stop the server...");
        System.in.read();
        server.shutdown();
        System.out.println("Server shut down");
    }

    @Provider
    public static class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
            response.getHeaders().add("Access-Control-Allow-Origin", "*");
            response.getHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }

}
