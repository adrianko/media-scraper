import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Helper {

    public static String getCurrentIP() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                new URL("http://wtfismyip.com/text").openConnection().getInputStream()
            ));

            StringBuilder contents = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                contents.append(line);
            }

            br.close();

            return contents.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Document retrievePage(String url) {
        try {
            return Jsoup.connect(url).timeout(Integer.parseInt(Scraper.settings.get("timeout"))).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void loadSettings() {
        try (Statement s = DB.get().createStatement()) {
            ResultSet dbSettings = s.executeQuery("SELECT * FROM settings");

            while (dbSettings.next()) {
                Scraper.settings.put(dbSettings.getString("property"), dbSettings.getString("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}