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
                c = DriverManager.getConnection("jdbc:sqlite:" + path + (Scraper.debug ? "test" : "shows") + ".db");
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
    
    public static void bump(Show s) {
        try {
            PreparedStatement update = get().prepareStatement("UPDATE shows SET episode = ? WHERE title = ?");
            update.setInt(1, s.getEpisode() + 1);
            update.setString(2, s.getTitle());
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void nextSeason(Show s) {
        try {
            PreparedStatement update = get().prepareStatement("UPDATE shows SET episode = 1, season = ? WHERE " +
                "title = ?");
            update.setInt(1, s.getSeason() + 1);
            update.setString(2, s.getTitle());
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Show> Map<String, Show> getShows(Class<T> c) {
        Map<String, Show> shows = new HashMap<>();
        String prefix = c.getName().toLowerCase().substring(5, 7);

        try {
            ResultSet rs = get().createStatement().executeQuery("SELECT * FROM shows");

            while (rs.next()) {
                shows.put(
                    rs.getString("title"),
                    c.getConstructor(String.class, String.class, String.class, int.class, int.class, int.class, 
                        int.class)
                    .newInstance(rs.getString("title"), Helper.settings.get(prefix + "_base") + 
                            rs.getString(prefix + "_url"), Helper.settings.get(prefix + "_ep"), rs.getInt("season"), 
                        rs.getInt("episode"), rs.getInt("hd"), rs.getInt("runtime"))
                );
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | 
            SQLException e) {
            e.printStackTrace();
        }

        return shows;
    }

}