/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.TCPServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author schubert
 */
public class RequestHandler implements Runnable{
    //client socket to read the request 
    private Socket socket;
    //Server data being handed down from the listener
    private String data;
    //read for running the RequestHandler
    private Thread thread;
    //Name of the thread
    private String threadName;
    //number for generating new ThreadIDs
    private static long threadID = 0;
    
    
    
    public RequestHandler(Socket socket,String serverData){
       this.socket = socket;
       this.data = serverData;
    }
    
    /***
     * Read the request from the socket and output the result as a GeoJson
     */
    
    
    public void run(){
        InputStream in = null;
        DataOutputStream out = null;
        try {
            System.out.println("[Request Handler #"+threadID+"] Thread run");
            in = socket.getInputStream();
            //ObjectMapper mapper = new ObjectMapper();
            DataInputStream ois = new DataInputStream(in);
            String requestString = ois.readUTF();
            
            
            //################################
            System.err.println("[Request Handler #"+threadID+"] Server Request: "+requestString);
            //################################//            System.err.println(response);
//            out = new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()));
//            out.write(response);

            DijkstraHandler dijkstra = new DijkstraHandler();
            String response = dijkstra.startDijkstraSearch(requestString);
            
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(response);
            out.flush();
            out.close();
            System.out.println("[Request Handler #"+threadID+"] Thread closed");
        } catch (IOException e) {
            System.err.println("IOException occurred while processing request.");
            e.printStackTrace();
        }
    }
    
    /***
     * Start a new runnable
     */
       
    public void start(){
        threadID++;
        String threadName = "RequestHandler"+(threadID-1);
        thread = new Thread(this,threadName);
        thread.start();
    }
}
