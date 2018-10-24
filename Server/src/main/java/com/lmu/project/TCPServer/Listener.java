/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.TCPServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author schubert
 */
public class Listener implements Runnable {
    //Thread object for running the listener
    private Thread t;    
    //data handed down from the server
    public String serverData ;
    //flag to stop the thread
    private boolean stop;
    //port the listener is listening on
    private int PORT;
    
    public Listener(int port,String servData){
        this.PORT = port;       
        serverData = servData;
    }
    
    /***
     * Listen on the ServerSocket  and start RequestHandlers
     */        
    public void run() {
                stop = false;
                try {
                    ServerSocket serverSocket = new ServerSocket(PORT);
                    while (!stop) {
                        final Socket clientSocket = serverSocket.accept();
                        RequestHandler handler = new RequestHandler( clientSocket, serverData);
                        handler.start();
                    }
                    System.out.println("[Listener] Stopping Listener");
                } catch (IOException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);                    
                }
            }
    /***
     * Start up the listener if none is running
     * 
     */    
    public void start(){     
      System.out.println("[Listener] Starting Listener" );
      if (t == null) {
         t = new Thread (this,"Listener");
         t.start ();
      }
    }    
    
    /**
     * tells the thread to exit the loop
     */
    
    public void shutdown() {
       stop = true;
       System.out.println("[Listener] Listener shut down");
    }
    
}
