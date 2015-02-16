import java.sql.*;

public class DB {

    private static Connection c = null;

    public static Connection get() {
        if (c == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:" + DB.class.getResource(".").getPath() + "../../../db/shows.db");
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

}
