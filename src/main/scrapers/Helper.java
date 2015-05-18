package main.scrapers;

import main.scrapers.tv.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");

    public static String getCurrentIP() {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://wtfismyip.com/text")
                .openConnection().getInputStream()))) {
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public static void checkIP() {
        if (settings.get("ip").equals(getCurrentIP())) {
            Scraper.logger.severe("Wrong IP, exiting...");
            System.exit(0);
        }
    }

    public static void checkOS() {
        if (!isWindows) {
            Scraper.logger.severe("This application is not designed to run on any operating system other than Window" +
                    "s. Sorry.");
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

    public static void loadGlobalSettings() {
        try (Statement s = Database.get("settings").createStatement()) {
            ResultSet dbSettings = s.executeQuery("SELECT * FROM settings");

            while (dbSettings.next()) {
                settings.put(dbSettings.getString("property"), dbSettings.getString("value"));
            }

            dbSettings.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static String retrieveTitle(Element e) {
        return e.select("td").first().select(".torrentname").first().select("a.cellMainLink").first().text();
    }
    
    public static String retrieveMagnet(Element e) {
        return e.select("td").first().select(".iaconbox").first().select("a.imagnet").first().attr("href").split("&")[0];
    }

}
