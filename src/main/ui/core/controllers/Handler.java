package main.ui.core.controllers;

import com.sun.net.httpserver.HttpExchange;
import main.ui.HTTPServer;
import main.ui.core.components.Controller;
import main.ui.core.components.Helper;
import main.ui.core.components.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Handler extends Controller {

    @Override
    public void handle(HttpExchange t) {
        HTTPServer.logger.info(t.getRequestURI().toString());
        String url = t.getRequestURI().toString();

        if (url.equals("/")) {
            try {
                HTTPServer.controllers.get(HTTPServer.MAIN_CONTROLLER).handle(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (Files.exists(Paths.get(Helper.getFilePath(url)))) {
            Response.sendFile(t, Helper.getFilePath(url));
        } else {
            Response.sendHTML(t, "1");
        }
    }

}