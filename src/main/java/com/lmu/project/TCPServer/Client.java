/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.TCPServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Test client to access TCP Server
 * @author schubert
 */
public class Client {
    public static void main(String[] args){
        Socket socket =null; 
        try {
            socket = new Socket("localhost", 9595);
           
        DataOutputStream  oos = new DataOutputStream(socket.getOutputStream());
        
        String json = "{\"s\":{\"lat\":54.47939502777519,\"lon\":9.82977032661438},\"t\":{\"lat\":54.48208153819395,\"lon\":9.830135107040405}}";
        
        //
        //oos.writeUTF("[REQUEST ...]");
        oos.writeUTF(json);
        oos.flush();
        DataInputStream ois = new DataInputStream(socket.getInputStream());
        String response = (String) ois.readUTF();
        System.err.println(response);

        } catch (UnknownHostException e) {
            System.out.println("Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null)
                try {
                    socket.close();
                    System.out.println("Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("Socket nicht zu schliessen...");
                    e.printStackTrace();
                }
        } 
    }
}
