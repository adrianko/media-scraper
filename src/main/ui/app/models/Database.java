package main.ui.app.models;

import main.scrapers.movie.orm.Movie;
import main.ui.Base;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Database {
    
    static Map<String, Connection> connections = new HashMap<>();
    
    public static Connection getConnection(String db) {
        if (!connections.containsKey(db)) {
            if (Files.exists(Paths.get(Base.path + "/db/" + db + ".db"))) {
                try {
                    Class.forName("org.sqlite.JDBC");
                    connections.put(db, DriverManager.getConnection("jdbc:sqlite:" + Base.path + "/db/" + db + ".db"));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return connections.containsKey(db) ? connections.get(db) : null;
    }
    
    public static List<Show> getTVShows() {
        List<Show> shows = new LinkedList<>();
        
        try {
            ResultSet rs = getConnection("shows").createStatement().executeQuery("SELECT s.*, r.runtime AS " +
                    "'runtime_min' FROM shows AS s LEFT JOIN runtimes AS r ON r.id = s.runtime");
            
            while (rs.next()) {
                shows.add(new Show(rs.getInt("id"), rs.getString("title"), rs.getString("ka_url"), rs.getInt("rb_url"), 
                        rs.getInt("season"), rs.getInt("episode"), rs.getInt("hd"), rs.getInt("runtime_min")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return shows;
    }
    
    public static List<Movie> getMovies() {
        List<Movie> movies = new LinkedList<>();
        
        try {
            ResultSet rs = getConnection("movies").createStatement().executeQuery("SELECT * FROM movies");
            
            while (rs.next()) {
                movies.add(new Movie(rs.getString("title"), rs.getInt("year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return movies;
    }
    
    public static List<Setting> getSettings(String db) {
        List<Setting> settings = new LinkedList<>();
        
        try {
            ResultSet rs = getConnection(db).createStatement().executeQuery("SELECT * FROM settings");
            
            while (rs.next()) {
                settings.add(new Setting(rs.getString("property"), rs.getString("value")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return settings;
    }

    public static class Show {

        protected int id;
        protected String title;
        protected String kaURL;
        protected int rbURL;
        protected int season;
        protected int episode;
        protected int hd;
        protected int runtime;
        protected String quality;

        public Show(int i, String t, String ka, int rb, int s, int e, int h, int r) {
            id = i;
            title = t;
            kaURL = ka;
            rbURL = rb;
            season = s;
            episode = e;
            hd = h;
            runtime = r;
            setQuality();
        }
        
        public int getHD() {
            return hd;
        }

        public void setQuality() {
            switch (getHD()) {
                case 1:
                    quality = "1080p";
                    break;
                case 2:
                    quality = "720p";
                    break;
                default:
                    quality = "HDTV";
                    break;
            }
        }

    }
    
    public static class Setting {
        
        public String property;
        public String value;
        
        public Setting(String p, String v) {
            property = p;
            value = v;
        }
        
    }
    
}