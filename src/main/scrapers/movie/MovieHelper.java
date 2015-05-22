package main.scrapers.movie;

import main.scrapers.Helper;
import main.scrapers.Database;
import main.scrapers.movie.orm.CacheItem;
import main.scrapers.movie.orm.Movie;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieHelper extends Helper {

    public static void loadSettings() {
        MovieScraper.logger.info("Loading settings");
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
            .replaceAll("(?i)\\(" + (m.getYear() + 1) + "\\)", "").replaceAll("(?i)\\(" + (m.getYear() - 1) +"\\)", "")
            .replaceAll("(?i)1080p", "").replaceAll("(?i)BrRip", "").replaceAll("(?i)x264", "")
            .replaceAll("(?i)- YIFY", "").replaceAll("(?i)-YIFY", "").trim();
        
        return t.equals("");
    }
    
    public static void buildCache() {
        MovieScraper.logger.info("Building Cache");
        Set<Long> cacheIDs = MovieDatabase.getCache().stream().map(CacheItem::getID).collect(Collectors.toSet());
        Set<CacheItem> allRetrieved = new HashSet<>();
        String url = settings.get("base_url");
        int page = 1;
        int duplicates = 0;
        
        while (duplicates <= 5) {
            Set<CacheItem> retrieved = parsePage(url + (page++));
            allRetrieved.addAll(retrieved);
            duplicates += retrieved.stream().filter(ci -> cacheIDs.contains(ci.getID())).count();
            cacheIDs.addAll(retrieved.stream().map(CacheItem::getID).collect(Collectors.toSet()));
        }
        
        MovieScraper.logger.info("Managed: " + page);
        allRetrieved.forEach(MovieDatabase::addCacheItem);
    }
    
    public static Set<CacheItem> parsePage(String url) {
        Set<CacheItem> cacheItems = new HashSet<>();
        Document doc = retrievePage(url);

        if (doc != null) {
            Elements tableRows = doc.select(".data").first().select(".odd, .even");

            tableRows.stream().filter(e -> e.attr("id") != null).forEach(e -> cacheItems.add(new CacheItem(Long
                .parseLong(e.attr("id").replaceAll("[\\D]", "")), retrieveTitle(e), retrieveMagnet(e))));
        }

        return cacheItems;
    }
    
    public static String sanitizeString(String str) {
        String clean = str.replaceAll("[^A-Za-z0-9]", "-").replaceAll("-+", "-").toLowerCase();
        clean = clean.startsWith("-") ? clean.substring(1) : clean;
        clean = clean.endsWith("-") ? clean.substring(0, clean.length() - 1) : clean;
        
        return clean;
    }

    public static boolean ifValidYear(boolean edge, CacheItem ci, Movie movie) {
        if (edge) {
            return (ci.getTitle().contains(String.valueOf(movie.getYear() + 1)) ||
                ci.getTitle().contains(String.valueOf(movie.getYear() - 1)));
        } else {
            return (ci.getTitle().contains(String.valueOf(movie.getYear())));
        }
    }

}