package main.ui.app;

import com.sun.net.httpserver.HttpExchange;
import main.scrapers.tv.Scraper;
import main.ui.core.components.Controller;
import main.ui.core.components.Response;

import java.io.IOException;

public class Controllers {

    public static class Home extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.send(t, "<h1>Media Scraper</h1>", "text/html");
        }

    }

    public static class Scrape extends Controller {

        @Override
        public void handle(HttpExchange t) throws IOException {
            Scraper.main(new String[]{});
            Response.send(t, "{success: 1}", "application/json");
        }

    }
    
}
