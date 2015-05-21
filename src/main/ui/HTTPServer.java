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
                System.out.println(c.getSimpleName() + ": " + c.getCanonicalName());
            });
            server.createContext("/", new Home());
            server.createContext("/scrape", new Scrape());
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

    public void sendResponse(HttpExchange t, String response, String contentType) {
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
    
    class Home implements HttpHandler {

        @Override
        public void handle(HttpExchange t) {
            sendResponse(t, "<h1>Media Scraper</h1>", "text/html");
        }
        
    }
    
    class Scrape implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            Scraper.main(new String[]{});
            sendResponse(t, "{success: 1}", "application/json");
        }
        
    }
    
}