package main.scrapers.movie;

import main.scrapers.movie.orm.CacheItem;
import main.scrapers.movie.orm.Movie;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * TODO add support for special character matching
 * TODO WebUI
 *  TODO Global settings page
 *  TODO Media specific settings
 *  TODO Media specific lists
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
        movie = match(filter(false, movie), movie);

        if (!movie.found()) {
            movie = match(filter(true, movie), movie);

            if (!movie.found()) {
                logger.info("None found.");
            }
        }
    }

    public Set<CacheItem> filter(boolean edge, Movie movie) {
        return cacheItems.stream().filter(ci -> ci.getTitle().contains(movie.getTitle()) &&
                ci.getTitle().contains("1080p") && ifValidYear(edge, ci, movie)).collect(Collectors.toSet());
    }

    public Movie match(Set<CacheItem> filtered, Movie movie) {
        filtered.stream().filter(ci -> MovieHelper.validateOption(ci, movie)).forEach(ci -> {
            MovieDownloader.enqueue(ci.getMagnet());
            mvd.setLabel(ci.getMagnet());
            MovieDatabase.markDone(movie);
            System.out.println("Found: " + ci.getTitle());
            movie.markFound();
        });

        return movie;
    }

    public boolean ifValidYear(boolean edge, CacheItem ci, Movie movie) {
        if (edge) {
            return (ci.getTitle().contains(String.valueOf(movie.getYear() + 1)) ||
                    ci.getTitle().contains(String.valueOf(movie.getYear() - 1)));
        } else {
            return (ci.getTitle().contains(String.valueOf(movie.getYear())));
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
