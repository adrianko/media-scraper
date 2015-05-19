package main.ui;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class HTTPServer {
    
    public static void main(String[] args) {
        try {
            HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 9898), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}