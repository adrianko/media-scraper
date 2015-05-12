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
import java.util.stream.Collectors;

public class MovieHelper extends Helper {

    public static void loadSettings() {
        Scraper.logger.info("Loading settings");
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
        Scraper.logger.info("Building Cache");
        Set<Integer> cacheIDs = MovieDatabase.getCache().stream().map(CacheItem::getID).collect(Collectors.toSet());
        Set<CacheItem> allRetrieved = new HashSet<>();
        String url = settings.get("base_url");
        int page = 1;
        int duplicates = 0;
        
        while (duplicates <= 5) {
            Set<CacheItem> retrieved = parsePage(url + (page++));
            allRetrieved.addAll(retrieved);
            duplicates += retrieved.stream().filter(ci -> !cacheIDs.contains(ci.getID())).count();
        }
        
        Scraper.logger.info("Managed: " + page);
        //send all retrieved to database
    }
    
    public static Set<CacheItem> parsePage(String url) {
        Set<CacheItem> cacheItems = new HashSet<>();
        Document doc = retrievePage(url);
        
        return cacheItems;
    }

}