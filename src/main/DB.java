package main;

import javax.xml.transform.Result;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DB {

    private static Connection c = null;

    public static Connection get() {
        if (c == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:" + DB.class.getResource(".").getPath() + "../../../../db/shows.db");
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

    public static ResultSet getShows() {
        try {
            return get().createStatement().executeQuery("SELECT * FROM shows");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
        public static <T extends Show> Map<String, Show> getShows(Class<T> c) {
        Map<String, Show> shows = new HashMap<>();
        String type = c.toString().substring(0, 2).toLowerCase();

        try {
            ResultSet rs = get().createStatement().executeQuery("SELECT * FROM shows");

            while(rs.next()) {
                try {
                    Show s = c.getConstructor(
                            String.class, String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class
                    ).newInstance(
                            rs.getString("title"),
                            Scraper.settings.get(type + "_base") + rs.getString(type + "_url"),
                            Scraper.settings.get(type + "_ep"),
                            rs.getInt("season"),
                            rs.getInt("episode"),
                            rs.getInt("hd"),
                            rs.getInt("runtime")
                    );
                    shows.put(rs.getString("title"), s);

                    /*
                    shows.put(rs.getString("title"),
                        new KAShow(
                            rs.getString("title"),
                            Scraper.settings.get(type + "_base") + rs.getString(type + "_url"),
                            Scraper.settings.get(type + "_ep"),
                            rs.getInt("season"),
                            rs.getInt("episode"),
                            rs.getInt("hd"),
                            rs.getInt("runtime")
                        )
                    );
                    */
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return shows;
    }

}
