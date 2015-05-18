package main.scrapers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Map<String, Connection> connections = new HashMap<>();

    public static Connection get(String db) {
        if (!connections.containsKey(db)) {
            try {
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection("jdbc:sqlite:" + Base.path + "/db/" + db + ".db");
                connections.put(db, c);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return connections.get(db);
    }

    public static void close(String db) {
        try {
            if (connections.containsKey(db)) connections.get(db).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
