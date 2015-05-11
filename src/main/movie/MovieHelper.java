package main.movie;

import main.Helper;
import main.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieHelper extends Helper {

    public static void loadSettings() {
        Helper.loadGlobalSettings();

        try (Statement s = Database.get("movies").createStatement()) {
            ResultSet dbSettings = s.executeQuery("SELECT * FROM settings");

            while (dbSettings.next()) {
                settings.put(dbSettings.getString("property"), dbSettings.getString("value"));
            }

            dbSettings.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}