package main;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DB {

    private static Connection c = null;
    private static String path = DB.class.getResource(".").getPath() + "../../../../db/";

    public static Connection get() {
        if (c == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                if (Scraper.test) {
                    c = DriverManager.getConnection("jdbc:sqlite:" + path + "test.db");
                } else {
                    c = DriverManager.getConnection("jdbc:sqlite:" + path + "shows.db");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return c;
    }

    public static void close() {
        try {
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void bump(String title) {
        try {
            PreparedStatement update = get().prepareStatement("UPDATE shows SET episode = ? WHERE title = ?");
            update.setInt(1, Scraper.shows.get(title).getEpisode() + 1);
            update.setString(2, title);
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void nextSeason(String title) {
        try {
            PreparedStatement update = get().prepareStatement("UPDATE shows SET episode = 1, season = ? WHERE title = ?");
            update.setInt(1, Scraper.shows.get(title).getSeason() + 1);
            update.setString(2, title);
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Show> Map<String, Show> getShows(Class<T> c) {
        Map<String, Show> shows = new HashMap<>();

        try {
            ResultSet rs = get().createStatement().executeQuery("SELECT * FROM shows");

            while (rs.next()) {
                shows.put(
                    rs.getString("title"),
                    c.getConstructor(String.class, String.class, String.class, int.class, int.class, int.class, int.class)
                    .newInstance(
                        rs.getString("title"), Helper.settings.get("ka_base") + rs.getString("ka_url"), Helper.settings.get("ka_ep"),
                        rs.getInt("season"), rs.getInt("episode"), rs.getInt("hd"), rs.getInt("runtime")
                    )
                );
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | SQLException e) {
            e.printStackTrace();
        }

        return shows;
    }

}