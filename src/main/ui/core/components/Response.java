package main.ui.core.components;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class Response {

    public static void send(HttpExchange t, String response, String contentType) {
        try {
            t.getResponseHeaders().add("Content-Type", contentType);
            t.sendResponseHeaders(200, response.length());
            t.getResponseBody().write(response.getBytes());
            t.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendJSON(HttpExchange t, Map<String, Object> json) {
        Response.send(t, new JSONObject(json).toString(), "application/json");
    }

    public static void sendHTML(HttpExchange t, String html) {
        Response.send(t, html, "text/html");
    }
    
}
