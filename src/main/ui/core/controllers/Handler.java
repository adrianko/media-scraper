package main.ui.core.controllers;

import com.github.mustachejava.DefaultMustacheFactory;
import com.sun.net.httpserver.HttpExchange;
import main.scrapers.Base;
import main.ui.HTTPServer;
import main.ui.core.components.Controller;
import main.ui.core.components.Helper;
import main.ui.core.components.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Handler extends Controller {

    @Override
    public void handle(HttpExchange t) {
        String url = t.getRequestURI().toString();

        if (url.equals("/") || url.equals("")) {
            try {
                HTTPServer.controllers.get(HTTPServer.MAIN_CONTROLLER).handle(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (Files.exists(Paths.get(Helper.getFilePath(url)))) {
            Response.sendFile(t, Helper.getFilePath(url));
        } else if (Files.exists(Paths.get(Base.path + "/views/404.mustache"))) {
            Writer w = new StringWriter();

            try {
                new DefaultMustacheFactory().compile(main.ui.Base.path + "/views/404.mustache").execute(w, 
                        new Error404()).flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            Response.send(t, w.toString(), 404, "text/html");
        } else {
            Response.sendHTML(t, "404");
        }
    }
    
    public static class Error404 {}

}