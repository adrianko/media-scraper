package main.scrapers.movie;

import main.scrapers.Helper;
import main.scrapers.Scraper;
import main.scrapers.movie.orm.CacheItem;
import main.scrapers.movie.orm.Movie;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * TODO add support for special character matching
 */
public class MovieScraper extends Scraper {

    public static boolean debug = false;
    public static Logger logger = Logger.getLogger(Scraper.class.getName());
    public MovieDownloader mvd = new MovieDownloader();
    Set<CacheItem> cacheItems;

    public MovieScraper() {
        MovieHelper.loadSettings();
        Helper.checkIP();
        MovieHelper.buildCache();
        cacheItems = MovieDatabase.getCache();
        MovieDatabase.getMovies().forEach(this::parse);
        logger.info("Exiting...");
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
            ci.getTitle().contains("1080p") && MovieHelper.ifValidYear(edge, ci, movie)).collect(Collectors.toSet());
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

}
