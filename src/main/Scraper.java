package main;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

/**
 * TODO add secondary source
 * TODO add connection check
 * TODO move hdtv up queue
 * TODO multi number episode
 * TODO check if dl HDTV when 1080p required
 */
public class Scraper {

    public static Set<String> found = new HashSet<>();
    public static boolean debug = false;
    public static Logger logger = Logger.getLogger(Scraper.class.getName());
    static FileHandler fh;
    
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
                    logger.info("No " + show.getQuality() + " found for: " + show);
                    
                    if (show.getHD() == 1) {
                        show.setHD(0);
                        parseOptions(show, episode);
                        
                        if (!show.getFound()) {
                            logger.info("No " + show.getQuality() + " found for: " + show);
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
                logger.info("Found " + show.getQuality() + ": " + option.getName() + " " + option.getMagnet());
                found.add(show.getTitle());

                if (!debug) {
                    Downloader.enqueue(option.getMagnet());
                }
                
                DB.bump(show, episode);
                show.setFound();
                break;
            }
        }
        
        return show;
    }

    public static void main(String[] args) {
        try {
            fh = new FileHandler(new File(Base.path + "scraper.log").getAbsolutePath(), true);
            logger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
            logger.info("Loaded");
            
            if (args.length > 0 && args[0].equals("test")) {
                debug = true;
                logger.info("Debug mode");
            }
            
            //Helper.checkOS();
            Helper.loadSettings();
            Helper.checkIP();
            new Scraper();
            DB.close();
            logger.info("Exiting...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}