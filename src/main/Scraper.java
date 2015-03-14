package main;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO add secondary source
 * TODO add connection check
 * TODO move hdtv up queue
 * TODO multi number episode
 */
public class Scraper {

    public static Set<String> found = new HashSet<>();
    public static boolean debug = false;
    
    public Scraper() {
        Arrays.asList(KAShow.class, RBShow.class).forEach(c -> parse(DB.getShows(c).entrySet().stream().filter(s ->
                !found.contains(s.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
    }
    
    public void parse(Map<String, Show> shows) {
        for (Show show : shows.values()) {
            show.parse();

            for (Episode episode : show.getEpisodes()) {
                episode.parse();
                parseOptions(show, episode);

                if (!show.getFound()) {
                    System.out.println("No " + show.getQuality() + " found for: " + show);
                    
                    if (show.getHD() == 1) {
                        show.setHD(0);
                        parseOptions(show, episode);
                        
                        if (!show.getFound()) {
                            System.out.println("No " + show.getQuality() + " found for: " + show);
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
                System.out.println("Found " + show.getQuality() + ": " + option.getName() + " " + option.getMagnet());
                found.add(show.getTitle());

                if (!debug) {
                    Downloader.enqueue(option.getMagnet());
                }

                DB.bump(show);
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