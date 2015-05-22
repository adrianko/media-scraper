package main.ui.core.components;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.ui.HTTPServer;

public abstract class Controller implements HttpHandler {
    
    public Controller() {
        HTTPServer.logger.info("Loading controller: " + this.getClass().getName());
    }

    @Override 
    public abstract void handle(HttpExchange httpExchange);
    
}