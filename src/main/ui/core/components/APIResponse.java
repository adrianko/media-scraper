package main.ui.core.components;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

public class APIResponse {
    
    private int success;
    private String request;
    private Object response;
    
    private HttpExchange httpExchange;
    
    public void create(String req, HttpExchange he) {
        request = req;
        httpExchange = he;
    }
    
    public void success() {
        success = 1;
    }
    
    public void fail() {
        success = 0;
    }
    
    public void addResponse(Object r) {
        response = r;
    }
    
    public void send() {
        Map<String, Object> response = new HashMap<>();
        response.put("request", request);
        response.put("success", success);
        response.put("response", this.response);
        
        Response.sendJSON(httpExchange, response);
    }
    
}
