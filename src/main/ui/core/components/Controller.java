package main.ui.core.components;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class Controller implements HttpHandler {
    
    public Controller() {}

    @Override 
    public void handle(HttpExchange httpExchange) throws IOException {}
    
}