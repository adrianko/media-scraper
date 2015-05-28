package main.ui.core.controllers;

import com.sun.net.httpserver.HttpExchange;
import main.scrapers.Base;
import main.ui.HTTPServer;
import main.ui.core.components.Controller;
import main.ui.core.components.Helper;
import main.ui.core.components.Response;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Handler extends Controller {

    @Override
    public void handle(HttpExchange t) {
        String url = t.getRequestURI().toString();

        if (url.equals("/") || url.equals("")) {
            Helper.redirectController(HTTPServer.MAIN_CONTROLLER, t);
        } else if (Files.exists(Paths.get(Helper.getFilePath(url)))) {
            Response.sendFile(t, Helper.getFilePath(url));
        } else if (Files.exists(Paths.get(Base.path + "/views/404.mustache"))) {
            Response.send(t, Helper.renderView("/views/404.mustache", new Error404()), 404, "text/html");
        } else {
            Response.send(t, "404 Not found", 404, "text/html");
        }
    }
    
    public static class Error404 {}

}