package main.ui;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class HTTPServer {
    
    private HttpServer server;
    
    public HTTPServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 9898), 0);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        server.stop(0);
    }
    
    public static void main(String[] args) {
        new HTTPServer();
    }
    
}