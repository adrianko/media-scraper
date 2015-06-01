package main.ui.app;

import com.sun.net.httpserver.HttpExchange;
import main.scrapers.movie.orm.Movie;
import main.scrapers.tv.TVScraper;
import main.ui.app.models.Database;
import main.ui.core.components.Controller;
import main.ui.core.components.Helper;
import main.ui.core.components.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Controllers {

    public static class Home extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.sendHTML(t, Helper.renderView("/views/home.mustache", new Home()));
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
        
        protected List<Class> routes = Arrays.asList(this.getClass().getDeclaredClasses());

        @Override
        public void handle(HttpExchange t) {
            String url = t.getRequestURI().toString();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", "1");
            response.put("request", url);
            
            List<String> request = Arrays.asList(url.split("/")).stream().filter(s -> !s.equals(""))
                    .collect(Collectors.toList());
            request.remove(0);
            Map<String, String> post = Helper.retrievePOSTData(t.getRequestBody(), t.getRequestHeaders());
            Map<String, String> get = Helper.retrieveGETData(url);
            
            Optional<Class> route = routes.stream().filter(c -> c.getSimpleName().toLowerCase().equals(request.get(0)
                    .toLowerCase())).findFirst();
            
            if (route.isPresent()) {
                try {
                    Class c = Class.forName(route.get().getName());
                    Object r1 = c.newInstance();
                    Optional<Method> subRoute = Arrays.asList(route.get().getMethods()).stream().filter(m -> m.getName()
                            .toLowerCase().equals(request.get(1).toLowerCase())).findAny();

                    if (subRoute.isPresent()) {
                        if (subRoute.get().getParameterCount() == 0) {
                            response.put("test", subRoute.get().invoke(r1));
                        }
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            Response.sendJSON(t, response);
        }
        
        static class CRUD {
            public CRUD() {}
        }
        
        static class Settings extends CRUD {
            
            public int testMethod() {
                return 3;
            }
            
            public boolean add(String db, String property, String value) {
                return true;    
            }
            
        }
        
        static class TV extends CRUD {
            
            public boolean add(String title, int season, int episode, String quality, int runtime) {
                return true;
            }
            
        }

    }
    
    public static class Shows extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.sendHTML(t, Helper.renderView("/views/shows.mustache", new Shows()));
        }

        List<Database.Show> shows() {
            return Database.getTVShows();
        }

    }
    
    public static class Movies extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.sendHTML(t, Helper.renderView("/views/movies.mustache", new Movies()));
        }

        List<Movie> movies() {
            return Database.getMovies();
        }
        
    }
    
    public static class Settings extends Controller {

        @Override
        public void handle(HttpExchange t) {
            Response.sendHTML(t, Helper.renderView("/views/settings.mustache", new Settings()));
        }

        List<SettingsWrapper> wrapper() {
            return Arrays.asList(
                    new SettingsWrapper("Global", Database.getSettings("settings")),
                    new SettingsWrapper("Shows", Database.getSettings("shows")),
                    new SettingsWrapper("Movies", Database.getSettings("movies"))
            );
        }

        class SettingsWrapper {

            public String title;
            public List<Database.Setting> settings;

            public SettingsWrapper(String t, List<Database.Setting> s) {
                title = t;
                settings = s;
            }

        }
        
    }
    
}