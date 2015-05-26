package main.ui.core.components;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Response {
    
    public static void setHeaders(HttpExchange t, byte[] response, String contentType) {
        try {
            t.getResponseHeaders().add("Content-Type", contentType);
            t.sendResponseHeaders(200, response.length);
            t.getResponseBody().write(response);
            t.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(HttpExchange t, String response, String contentType) {
        setHeaders(t, response.getBytes(), contentType);
    }

    public static void sendJSON(HttpExchange t, Map<String, Object> json) {
        Response.send(t, new JSONObject(json).toString(), "application/json");
    }

    public static void sendHTML(HttpExchange t, String html) {
        Response.send(t, html, "text/html");
    }

    public static void sendFile(HttpExchange t, String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] file = Files.readAllBytes(path);
            setHeaders(t, file, Files.probeContentType(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
