package main.scrapers.tv;

import main.scrapers.Helper;
import main.scrapers.tv.shows.DownloadOption;
import main.scrapers.tv.shows.Episode;
import main.scrapers.tv.shows.Show;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TVHelper extends Helper {

    public static Map<Integer, HashMap<String, Long>> expectedFileSize = new HashMap<>();

    public static void loadSettings() {
        Helper.loadGlobalSettings();

        try (Statement s = TVDatabase.get("shows").createStatement()) {
            ResultSet dbSettings = s.executeQuery("SELECT * FROM settings");

            while (dbSettings.next()) {
                settings.put(dbSettings.getString("property"), dbSettings.getString("value"));
            }

            dbSettings.close();
            ResultSet dbRuntimes = s.executeQuery("SELECT * FROM runtimes");

            while (dbRuntimes.next()) {
                expectedFileSize.put(dbRuntimes.getInt("id"), new HashMap<>());

                for (String t : new String[]{"hdtv_min", "hdtv_max", "720p_min", "720p_max", "1080p_min", "1080p_max"}) {
                    expectedFileSize.get(dbRuntimes.getInt("id")).put(t, dbRuntimes.getLong(t));
                }
            }

            dbRuntimes.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean validateOption(DownloadOption t, Episode e, Show s) {
        if (t.getName().toLowerCase().contains("xvid")) return false;

        if (t.getName().contains("ReEnc")) return false;
        
        if (t.getName().contains("avi")) return false;

        if (t.getName().contains("NL Subs")) return false;

        if (!t.getName().contains(s.getQuality())) return false;

        if (t.getByteSize() < expectedFileSize.get(s.getRuntime()).get(s.getQuality().toLowerCase() + "_min")) 
            return false;

        if (t.getByteSize() > expectedFileSize.get(s.getRuntime()).get(s.getQuality().toLowerCase() + "_max")) 
            return false;
        
        return true;
    }

}