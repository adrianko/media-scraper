package main.scrapers.tv;

import main.scrapers.Database;
import main.scrapers.Downloader;
import main.scrapers.Helper;
import main.scrapers.Scraper;
import main.scrapers.tv.shows.DownloadOption;
import main.scrapers.tv.shows.Episode;
import main.scrapers.tv.shows.KAShow;
import main.scrapers.tv.shows.RBShow;
import main.scrapers.tv.shows.Show;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO add secondary source
 * TODO add connection check
 */
public class TVScraper extends Scraper {

    public TVDownloader tvd = new TVDownloader();
    public static Set<String> found = new HashSet<>();
    
    public TVScraper() {
        TVHelper.loadSettings();
        Helper.checkIP();
        Arrays.asList(KAShow.class, RBShow.class).forEach(c -> parse(TVDatabase.getShows(c).entrySet().stream().filter(s 
            -> !found.contains(s.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
        Database.close("shows");
        Database.close("settings");
        logger.info("Exiting...");
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
                    //Downloader.enqueue(option.getMagnet());
                    //tvd.setLabel(option.getMagnet());
                    TVDatabase.bump(show, episode);
                }

                show.setFound();
                break;
            }
        }
        
        return show;
    }

}