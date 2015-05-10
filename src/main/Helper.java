package main;

import main.tv.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

}
