package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Helper {

    public static Map<String, String> settings = new HashMap<>();
    public static Map<Integer, HashMap<String, Long>> expectedFileSize = new HashMap<>();

    public static String getCurrentIP() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://wtfismyip.com/text")
                .openConnection().getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
    
    public static void checkIP() {
        if (settings.get("ip").equals(getCurrentIP())) {
            System.out.println("Wrong IP, exiting...");
            System.exit(0);
        }
    }
    
    public static void checkOS() {
        if (!System.getProperty("os.name").contains("Windows")) {
            System.out.println("This application is not designed to run on any operating system other than Windows. Sorry.");
            System.exit(0);
        }
    }

    public static Document retrievePage(String url) {
        try {
            return Jsoup.connect(url).timeout(Integer.parseInt(settings.get("timeout"))).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void loadSettings() {
        try (Statement s = DB.get().createStatement()) {
            ResultSet dbSettings = s.executeQuery("SELECT * FROM settings");

            while (dbSettings.next()) {
                settings.put(dbSettings.getString("property"), dbSettings.getString("value"));
            }

            dbSettings.close();
            ResultSet dbRuntimes = s.executeQuery("SELECT * FROM runtimes");

            while (dbRuntimes.next()) {
                expectedFileSize.put(dbRuntimes.getInt("id"), new HashMap<>());

                for (String t : new String[]{"hdtv_min", "hdtv_max", "1080p_min", "1080p_max"}) {
                    expectedFileSize.get(dbRuntimes.getInt("id")).put(t, dbRuntimes.getLong(t));
                }
            }

            dbRuntimes.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean validateOption(DownloadOption t, Episode e, Show s) {
        if (!t.getName().contains(s.getQuality())) return false;

        if (t.getName().contains("ReEnc")) return false;

        if (e.getEpisode() != s.getEpisode() || e.getSeason() != s.getSeason())  {
            if (e.getEpisode() != 1 && (s.getSeason() + 1) != e.getSeason()) return false;
            DB.nextSeason(s.getTitle());
        }

        if (t.getName().contains("720p")) return false;

        if (t.getByteSize() < expectedFileSize.get(s.getRuntime()).get(s.getQuality().toLowerCase() + "_min")) return false;

        if (t.getByteSize() > expectedFileSize.get(s.getRuntime()).get(s.getQuality().toLowerCase() + "_max")) return false;
        
        return true;
    }

}