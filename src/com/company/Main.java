package com.company;
import java.io.IOException;



public class Main {

    public static Server server = new Server();



    public static void main(String[] args) throws IOException {
        System.out.println("Starting server...");
        System.out.println("Server started.");
        while(true){
            server.start();
        }
    }
}
