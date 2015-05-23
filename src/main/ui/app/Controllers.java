package main.ui.app;

import com.sun.net.httpserver.HttpExchange;
import main.scrapers.tv.TVScraper;
import main.ui.core.components.Controller;
import main.ui.core.components.Helper;
import main.ui.core.components.Response;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Controllers {

    public static class Home extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.send(t, "<h1>Media Scraper</h1>", "text/html");
        }

    }

    public static class Scrape extends Controller {

        @Override
        public void handle(HttpExchange t) {
            TVScraper.main(new String[]{});
            Response.send(t, "{\"success\": 1}", "application/json");
        }

    }

    public static class API extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", "1");
            response.put("request", t.getRequestURI().toString());
            Helper.retrievePOSTData(t.getRequestBody());
            //t.getRequestHeaders().forEach((k, v) -> System.out.println(k + ": " + v));
            Response.send(t, new JSONObject(response).toString(), "application/json");
        }

    }
    
}