import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Downloader {
    
    public static void enqueue(String magnet) {

    }

    private static String getAddURL(String magnet) {
        try {
            return "http://" + Scraper.settings.get("dl_user") + ":" + Scraper.settings.get("dl_pass") + "@" +
                Scraper.settings.get("dl_host") + ":" + Scraper.settings.get("dl_port") + "/gui/?action=add-url&s=" +
                URLEncoder.encode(magnet, "US-ASCII") + "&t=" + System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }
    
}