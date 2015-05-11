package main.movie;

import main.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class MovieDatabase extends Database {

    public static Set<Movie> getMovies() {
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

    public boolean addCacheItem(CacheItem ci) {
        try {
            PreparedStatement check = get("movies").prepareStatement("SELECT * FROM cache WHERE id=?");
            check.setInt(1, ci.getID());
            ResultSet checkRS = check.executeQuery();
            int rowCount = checkRS.last() ? checkRS.getRow() : 0;

            if (rowCount == 0) {
                PreparedStatement add = get("movies").prepareStatement("INSERT INTO cache (id, title, magnet) VALUES " +
                        "(?, ?, ?)");
                add.setInt(1, ci.getID());
                add.setString(2, ci.getTitle());
                add.setString(3, ci.getMagnet());
                add.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}