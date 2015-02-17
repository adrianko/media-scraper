package main;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Double quality download
 * TODO add secondary source
 * TODO add connection check
 * TODO SD precedence HD
 * TODO 720 categorization
 * TODO multi number episode
 */
public class Scraper {

    public static Map<String, String> settings = new HashMap<>();
    public static Map<Integer, HashMap<String, Long>> expectedFileSize = new HashMap<>();
    public static Map<String, Show> shows = new HashMap<>();

    public static void main(String[] args) {
        if (!System.getProperty("os.name").contains("Windows")) {
            System.out.println("This application is not designed to run on any operating system other than Windows. Sorry.");
            System.exit(0);
        }

        Helper.loadSettings();
        Helper.checkIP();
        shows = DB.getShows(KAShow.class);
        
        for (Show show : shows.values()) {
            show.parse();

            for (Episode e : show.getEpisodes()) {

                for (DownloadOption t : e.getOptions()) {
                    if (Helper.validateOption(t, e, show)) {
                        System.out.println("Found: " + t.getName() + " " + t.getMagnet());
                        Downloader.enqueue(t.getMagnet());
                        DB.bump(show.getTitle());
                        show.setFound();
                        break;
                    }
                }

                if (!show.getFound()) {
                    System.out.println("None found for: " + show.getTitle() +
                            " S" + (show.getSeason() < 10 ? "0" : "") + show.getSeason() +
                            "E" + (show.getEpisode() < 10 ? "0" : "") + show.getEpisode());
                }
            }
        }
        
        DB.close();
    }

}