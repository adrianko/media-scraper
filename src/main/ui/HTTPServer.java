package main.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.scrapers.tv.Scraper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class HTTPServer {
    
    private HttpServer server;
    
    public HTTPServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 9898), 0);
            server.createContext("/", new Home());
            server.createContext("/scrape", new Scrape());
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
    
    class Home implements HttpHandler {

        @Override
        public void handle(HttpExchange t) {
            String response = "Hello";
            
            try {
                t.getResponseHeaders().add("Content-Type", "text/html");
                t.sendResponseHeaders(200, response.length());
                t.getResponseBody().write(response.getBytes());
                t.getResponseBody().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    class Scrape implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            Scraper.main(new String[]{});

            try {
                String response = "1";
                t.getResponseHeaders().add("Content-Type", "text/html");
                t.sendResponseHeaders(200, response.length());
                t.getResponseBody().write(response.getBytes());
                t.getResponseBody().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
}