package main.movie;

import main.movie.orm.CacheItem;
import main.movie.orm.Movie;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * TODO Remove completed requests
 * TODO Add processing messages when matching
 * TODO Add edge case for year if not found
 */
public class Scraper {
    
    public static boolean debug = false;
    public static Logger logger = Logger.getLogger(Scraper.class.getName());
    public MovieDownloader mvd = new MovieDownloader();
    Set<CacheItem> cacheItems;

    public Scraper() {
        MovieHelper.buildCache();
        cacheItems = MovieDatabase.getCache();
        MovieDatabase.getMovies().forEach(this::parse);
    }

    public void parse(Movie movie) {
        logger.info("Scraping: " + movie.getTitle() + " / " + movie.getYear());
        List<CacheItem> filtered = cacheItems.stream().filter(ci ->
                ci.getTitle().contains(movie.getTitle()) &&
                ci.getTitle().contains(String.valueOf(movie.getYear())) &&
                ci.getTitle().contains("1080p"))
            .collect(Collectors.toList());

        if (!filtered.isEmpty()) {
            filtered.stream().filter(ci -> MovieHelper.validateOption(ci, movie)).forEach(ci -> {
                MovieDownloader.enqueue(ci.getMagnet());
                mvd.setLabel(ci.getMagnet());
                MovieDatabase.markDone(movie);
            });
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            debug = true;
            logger.info("Debug mode");
        }

        MovieHelper.loadSettings();
        new Scraper();
        logger.info("Exiting...");
    }

}
