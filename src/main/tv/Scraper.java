package main.tv;

import main.tv.objects.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * TODO add secondary source
 * TODO add connection check
 */
public class Scraper {

    public static Set<String> found = new HashSet<>();
    public static boolean debug = false;
    public static Logger logger = Logger.getLogger(Scraper.class.getName());
    public TVDownloader tvd = new TVDownloader();
    
    public Scraper() {
        Arrays.asList(KAShow.class, RBShow.class).forEach(c -> parse(TVDatabase.getShows(c).entrySet().stream().filter(s ->
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
                }

                if (episode.getEpisode() <= show.getEpisode()) {
                    break;
                }
            }
        }
    }
    
    public Show parseOptions(Show show, Episode episode) {
        for (DownloadOption option : episode.getOptions()) {
            if (TVHelper.validateOption(option, episode, show)) {
                if (show.getSeason() < episode.getSeason() && episode.getEpisode() >= 1) {
                    TVDatabase.nextSeason(show);
                }

                logger.info("Found " + show.getQuality() + ": " + option.getName() + " " + option.getMagnet());
                found.add(show.getTitle());

                if (!debug) {
                    TVDownloader.enqueue(option.getMagnet());
                    tvd.setLabel(option.getMagnet());
                    TVDatabase.bump(show, episode);
                }

                show.setFound();
                break;
            }
        }
        
        return show;
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            debug = true;
            logger.info("Debug mode");
        }

        //Helper.checkOS();
        TVHelper.loadGlobalSettings();
        TVHelper.loadSettings();
        TVHelper.checkIP();
        new Scraper();
        TVDatabase.close("shows");
        TVDatabase.close("settings");
        logger.info("Exiting...");
    }

}