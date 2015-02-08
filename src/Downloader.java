import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public class Downloader {
    
    public static void enqueue(String magnet) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(getAddURL(magnet)).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((Scraper.settings
                    .get("dl_user") + ":" + Scraper.settings.get("dl_pass")).getBytes())));
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