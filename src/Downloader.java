import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public class Downloader {

    private static boolean running() {
        boolean found = false;
        
        try {
            Process p = Runtime.getRuntime().exec("ps");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains(Scraper.settings.get("dl_exe"))) {
                    found = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    public static void enqueue(String magnet) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(getAddURL(magnet)).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((Scraper.settings
                    .get("dl_user") + ":" + Scraper.settings.get("dl_pass")).getBytes())));
            con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAddURL(String magnet) {
        try {
            return "http://" + Scraper.settings.get("dl_host") + ":" + Scraper.settings.get("dl_port") + "/gui/?action=add-url&s=" +
                URLEncoder.encode(magnet, "US-ASCII") + "&t=" + System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }
    
}