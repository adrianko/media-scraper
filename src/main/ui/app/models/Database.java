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
    
    public List<Show> getTVShows() {
        return new LinkedList<>();
    }
    
    public List<Show> getMovies() {
        return new LinkedList<>();
    }
    
    public Map<String, String> getSettings(String db) {
        Map<String, String> settings = new HashMap<>();
        
        if (Files.exists(Paths.get(Base.path + db + ".db"))) {
            try {
                Connection c = DriverManager.getConnection("jdbc:sqlite:" + Base.path + "/db/" + db + ".db");
                ResultSet rs = c.createStatement().executeQuery("SELECT * FROM settings");
                
                while (rs.next()) {
                    settings.put(rs.getString("property"), rs.getString("value"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return settings;
    }
    
    public Map<String, String> getGlobalSettings() {
        return new HashMap<>();
    }
    
}
