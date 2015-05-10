package main.movie;

import main.Database;

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

}