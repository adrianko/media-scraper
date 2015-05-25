package main.ui.core.controllers;

import com.sun.net.httpserver.HttpExchange;
import main.ui.HTTPServer;
import main.ui.core.components.Controller;
import main.ui.core.components.Response;

import java.io.IOException;

public class Handler extends Controller {

    @Override
    public void handle(HttpExchange t) {
        HTTPServer.logger.info(t.getRequestURI().toString());

        if (t.getRequestURI().toString().equals("/")) {
            try {
                HTTPServer.controllers.get(HTTPServer.MAIN_CONTROLLER).handle(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Response.sendHTML(t, "1");
        }
    }

}