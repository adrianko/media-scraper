package main.movie;

import main.Database;
import main.movie.orm.CacheItem;
import main.movie.orm.Movie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class MovieDatabase extends Database {

    public static Set<Movie> getMovies() {
        Scraper.logger.info("Retrieving request list");
        Set<Movie> movies = new HashSet<>();

        try {
            ResultSet rs = get("movies").createStatement().executeQuery("SELECT * FROM movies");

            while (rs.next()) {
                movies.add(new Movie(rs.getString("title"), rs.getInt("year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static Set<CacheItem> getCache() {
        Scraper.logger.info("Retrieving cache");
        Set<CacheItem> cache = new HashSet<>();

        try {
            ResultSet rs = get("movies").createStatement().executeQuery("SELECT * FROM cache");

            while (rs.next()) {
                cache.add(new CacheItem(rs.getInt("id"), rs.getString("title"), rs.getString("magnet")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cache;
    }

    public static boolean addCacheItem(CacheItem ci) {
        int rowCount = -1;

        try {
            PreparedStatement check = get("movies").prepareStatement("SELECT COUNT(*) AS 'count' FROM cache WHERE id=?");
            check.setLong(1, ci.getID());
            ResultSet checkRS = check.executeQuery();
            checkRS.next();
            rowCount = checkRS.getInt("count");

            if (rowCount == 0) {
                PreparedStatement add = get("movies").prepareStatement("INSERT INTO cache (id, title, magnet) VALUES " +
                        "(?, ?, ?)");
                add.setLong(1, ci.getID());
                add.setString(2, ci.getTitle());
                add.setString(3, ci.getMagnet());
                add.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (rowCount == 0);
    }

}