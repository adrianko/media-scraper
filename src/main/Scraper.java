package main;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO add secondary source
 * TODO add connection check
 * TODO move hdtv up queue
 * TODO multi number episode
 */
public class Scraper {
    
    public static Map<String, Show> shows = new HashMap<>();
    public static boolean test = false;
    
    public Scraper() {
        shows = DB.getShows(KAShow.class);

        for (Show show : shows.values()) {
            show.parse();

            for (Episode episode : show.getEpisodes()) {
                episode.parse();

                for (DownloadOption option : episode.getOptions()) {

                    if (Helper.validateOption(option, episode, show)) {
                        System.out.println("Found: " + option.getName() + " " + option.getMagnet());
                        //Downloader.enqueue(option.getMagnet());
                        DB.bump(show.getTitle());
                        show.setFound();
                        break;
                    }
                }

                if (!show.getFound()) {
                    System.out.println("None found for: " + show);
                }

                if (episode.getEpisode() <= show.getEpisode()) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            test = true;
        }

        //Helper.checkOS();
        Helper.loadSettings();
        Helper.checkIP();
        new Scraper();
        DB.close();
    }

}