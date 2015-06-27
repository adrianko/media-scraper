package main.scrapers.tv;

import main.scrapers.Database;
import main.scrapers.tv.shows.Episode;
import main.scrapers.tv.shows.Show;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class TVDatabase extends Database {

    public static void bump(Show s, Episode ep) {
        try {
            PreparedStatement currentEp = get("shows").prepareStatement("SELECT episode FROM shows WHERE title = ?");
            currentEp.setString(1, s.getTitle());
            ResultSet rs = currentEp.executeQuery();
            rs.next();
            int episodeNum = rs.getInt("episode");
            rs.close();

            if (ep.getEpisode() >= episodeNum) {
                PreparedStatement update = get("shows").prepareStatement("UPDATE shows SET episode = ? WHERE title = ?");
                update.setInt(1, ep.getEpisode() + 1);
                update.setString(2, s.getTitle());
                update.executeUpdate();
                update.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void nextSeason(Show s) {
        try {
            PreparedStatement update = get("shows").prepareStatement("UPDATE shows SET episode = 1, season = ? WHERE " +
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
        int ind = c.getName().lastIndexOf(".");
        String prefix = c.getName().substring(ind + 1, ind + 3).toLowerCase();

        try {
            ResultSet rs = get("shows").createStatement().executeQuery("SELECT * FROM shows WHERE id IN (1,3)");
            //ResultSet rs = get("shows").createStatement().executeQuery("SELECT * FROM shows");

            while (rs.next()) {
                shows.put(
                    rs.getString("title"),
                    c.getConstructor(String.class, String.class, String.class, int.class, int.class, int.class, 
                        int.class)
                    .newInstance(rs.getString("title"), TVHelper.settings.get(prefix + "_base") +
                            rs.getString(prefix + "_url"), TVHelper.settings.get(prefix + "_ep"), rs.getInt("season"),
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