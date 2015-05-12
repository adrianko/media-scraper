package main.movie;

import main.Helper;
import main.Database;
import main.movie.orm.CacheItem;
import main.movie.orm.Movie;
import org.jsoup.nodes.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class MovieHelper extends Helper {

    public static void loadSettings() {
        Helper.loadGlobalSettings();

        try (Statement s = Database.get("movies").createStatement()) {
            ResultSet dbSettings = s.executeQuery("SELECT * FROM settings");

            while (dbSettings.next()) {
                settings.put(dbSettings.getString("property"), dbSettings.getString("value"));
            }

            dbSettings.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean validateOption(CacheItem ci, Movie m) {
        String t = ci.getTitle().replaceAll("(?i)" + m.getTitle(), "").replaceAll("(?i)\\(" + m.getYear() + "\\)", "")
            .replaceAll("(?i)1080p", "").replaceAll("(?i)BrRip", "").replaceAll("(?i)x264", "")
            .replaceAll("(?i)- YIFY", "").replaceAll("(?i)-YIFY", "").trim();
        
        return t.equals("");
    }
    
    public static void buildCache() {
        String url = settings.get("base_url");
        
    }
    
    public static Set<CacheItem> parsePage(String url) {
        Set<CacheItem> cacheItems = new HashSet<>();
        Document doc = retrievePage(url);
        
        return cacheItems;
    }

}