package main;

import java.util.*;

/**
 * TODO add secondary source
 * TODO add connection check
 * TODO move hdtv up queue
 * TODO multi number episode
 */
public class Scraper {
    
    public static Map<String, Show> shows = new HashMap<>();
    public static Set<String> found = new HashSet<>();
    public static boolean debug = false;
    
    public Scraper() {
        List<Class> classes = new LinkedList<>(Arrays.asList(KAShow.class, RBShow.class));
        
        classes.forEach(c -> {
            //noinspection unchecked
            shows = DB.getShows(c);
            shows.keySet().stream().filter(found::contains).forEach(shows::remove);
            parse();
        });
        
    }
    
    public void parse() {
        for (Show show : shows.values()) {
            show.parse();

            for (Episode episode : show.getEpisodes()) {
                episode.parse();
                parseOptions(show, episode);

                if (!show.getFound()) {
                    System.out.println("None found for: " + show);
                    
                    if (show.getHD() == 1) {
                        show.setHD(0);
                        parseOptions(show, episode);
                        
                        if (!show.getFound()) {
                            System.out.println("No HDTV found for: " + show);
                            show.setHD(1);
                        }
                    }
                }

                if (episode.getEpisode() <= show.getEpisode()) {
                    break;
                }
            }
        }
    }
    
    public Show parseOptions(Show show, Episode episode) {
        for (DownloadOption option : episode.getOptions()) {

            if (Helper.validateOption(option, episode, show)) {
                System.out.println("Found: " + option.getName() + " " + option.getMagnet());
                found.add(show.getTitle());

                if (!debug) {
                    Downloader.enqueue(option.getMagnet());
                }

                DB.bump(show.getTitle());
                show.setFound();
                break;
            }
        }
        
        return show;
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            debug = true;
        }

        Helper.checkOS();
        Helper.loadSettings();
        Helper.checkIP();
        new Scraper();
        DB.close();
    }

}