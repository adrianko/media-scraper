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
            PreparedStatement retrieve = get().prepareStatement("SELECT episode FROM shows WHERE title = ?");
            retrieve.setString(1, title);
            ResultSet rs = retrieve.executeQuery();
            rs.first();
            
            PreparedStatement update = get().prepareStatement("UPDATE shows SET episode = ? WHERE title = ?");
            update.setInt(1, (rs.getInt("episode") + 1));
            update.setString(2, title);
            update.executeUpdate();
            
            rs.close();
            retrieve.close();
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
