package main.ui.app;

import com.sun.net.httpserver.HttpExchange;
import main.scrapers.movie.orm.Movie;
import main.scrapers.tv.TVScraper;
import main.ui.app.models.Database;
import main.ui.core.components.APIResponse;
import main.ui.core.components.Controller;
import main.ui.core.components.Helper;
import main.ui.core.components.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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
        protected APIResponse ar = new APIResponse();

        @Override
        public void handle(HttpExchange t) {
            String url = t.getRequestURI().toString();
            ar.create(url, t);
            List<String> request = Arrays.asList(url.split("/")).stream().filter(s -> !s.equals(""))
                    .collect(Collectors.toList());
            String last = request.get(request.size() - 1);
            
            if (last.contains("?")) {
                request.set(request.size() - 1, last.split("\\?")[0]);
            }

            request.remove(0); //remove "api"
            
            if (request.size() > 0) {
                Map<String, String> post = Helper.retrievePOSTData(t.getRequestBody(), t.getRequestHeaders());
                Map<String, String> get = Helper.retrieveGETData(url);

                Optional<Class> route = Helper.checkAPIRoute(routes, request);
                ar.fail();

                if (route.isPresent()) {
                    try {
                        CRUD rp1 = (CRUD) route.get().newInstance();

                        if (request.size() > 1) {
                            Optional<Method> subRoute = Helper.checkAPISubRoute(rp1, request);

                            if (subRoute.isPresent()) {
                                Method method = subRoute.get();
                                List<Object> args = new LinkedList<>(request.subList(2, request.size()));
                                rp1.clearParams();

                                if (args.size() == method.getParameterCount()) {
                                    if (!get.isEmpty()) {
                                        rp1.setGetParams(get);
                                    }

                                    if (!post.isEmpty()) {
                                        rp1.setPostParams(post);
                                    }

                                    ar.addResponse(method.invoke(rp1, args.toArray(new Object[args.size()])));
                                    ar.success();
                                } else {
                                    ar.addResponse("Path has incorrect number of parameters. Given: " + args.size() +
                                            ", Required: " + method.getParameterCount());
                                }
                            }
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            ar.send();
        }
        
        public void parse() {
            
        }
        
        static class CRUD {
            
            protected Map<String, String> get;
            protected Map<String, String> post;
            
            public CRUD() {}
            
            public void clearParams() {
                get = new HashMap<>();
                post = new HashMap<>();
            }

            public void setGetParams(Map<String, String> params) {
                get.putAll(params);
            }
            
            public void setPostParams(Map<String, String> params) {
                post.putAll(params);
            }
            
        }
        
        static class Settings extends CRUD {
            
            public int testMethod() {
                return 3;
            }
            
            public String testMethodParam(Object p1, Object p2) {
                return p1.toString() + p2.toString();
            }
            
            public String testGETParams() {
                return get.toString();
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