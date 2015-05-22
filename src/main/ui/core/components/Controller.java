package main.ui.core.components;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class Controller implements HttpHandler {
    
    public Controller() {}

    @Override 
    public abstract void handle(HttpExchange httpExchange);
    
}