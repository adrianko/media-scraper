package main.ui.app.models;

import main.scrapers.tv.shows.Show;
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
    
    private static Connection getConnection(String db) {
        if (!connections.containsKey(db)) {
            if (Files.exists(Paths.get(Base.path + db + ".db"))) {
                try {
                    connections.put(db, DriverManager.getConnection("jdbc:sqlite:" + Base.path + "/db/" + db + ".db"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return connections.get(db);
    }
    
    public static List<Show> getTVShows() {
        return new LinkedList<>();
    }
    
    public static List<Show> getMovies() {
        return new LinkedList<>();
    }
    
    public static Map<String, String> getSettings(String db) {
        Map<String, String> settings = new HashMap<>();
        
        try {
            ResultSet rs = getConnection(db).createStatement().executeQuery("SELECT * FROM settings");
            
            while (rs.next()) {
                settings.put(rs.getString("property"), rs.getString("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return settings;
    }
    
}