package main.ui.app;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sun.net.httpserver.HttpExchange;
import main.scrapers.tv.TVScraper;
import main.ui.Base;
import main.ui.core.components.Controller;
import main.ui.core.components.Helper;
import main.ui.core.components.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controllers {

    public static class Home extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.sendHTML(t, "<h1>Media Scraper</h1>");
        }

    }

    public static class Scrape extends Controller {

        @Override
        public void handle(HttpExchange t) {
            TVScraper.main(new String[]{});
            Map<String, Object> response = new HashMap<>();
            response.put("success", "1");
            Response.sendJSON(t, response);
        }

    }

    public static class API extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", "1");
            response.put("request", t.getRequestURI().toString());
            Helper.retrievePOSTData(t.getRequestBody(), t.getRequestHeaders());
            Response.sendJSON(t, response);
        }

    }
    
    public static class View extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.sendHTML(t, Helper.renderView("/views/example.mustache", new View()));
        }
        
        List<Item> items() {
            return Arrays.asList(new Item("Hello"), new Item("World"));
        }
        
        class Item {

            public String name;

            public Item(String n) {
                name = n;
            }
            
        }

    }
    
}