package main.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.scrapers.tv.Scraper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class HTTPServer {
    
    private HttpServer server;
    static final String NIC = "0.0.0.0";
    static final int PORT = 9898;
    
    
    public HTTPServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getByName(NIC), PORT), 0);
            Arrays.stream(HTTPServer.class.getDeclaredClasses()).forEach(c -> {
                try {
                    server.createContext("/" + c.getSimpleName().toLowerCase(), (HttpHandler) c.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            server.setExecutor(null);
            server.start();
            System.out.println("Starting Server " + NIC + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        server.stop(0);
    }

    public static void sendResponse(HttpExchange t, String response, String contentType) {
        try {
            t.getResponseHeaders().add("Content-Type", contentType);
            t.sendResponseHeaders(200, response.length());
            t.getResponseBody().write(response.getBytes());
            t.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new HTTPServer();
    }
    
    static class Home implements HttpHandler {
        
        public Home() {}

        @Override
        public void handle(HttpExchange t) {
            sendResponse(t, "<h1>Media Scraper</h1>", "text/html");
        }
        
    }
    
    static class Scrape implements HttpHandler {
        
        public Scrape() {}

        @Override
        public void handle(HttpExchange t) throws IOException {
            Scraper.main(new String[]{});
            sendResponse(t, "{success: 1}", "application/json");
        }
        
    }
    
}