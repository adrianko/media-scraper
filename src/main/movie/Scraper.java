package main.movie;

import java.util.logging.Logger;

public class Scraper {
    
    public static boolean debug = false;
    public static Logger logger = Logger.getLogger(Scraper.class.getName());
    public MovieDownloader mvd = new MovieDownloader();

    public Scraper() {
        MovieDatabase.getMovies().forEach(this::parse);
    }

    public void parse(Movie movie) {
        logger.info("Scraping: " + movie.getTitle() + " / " + movie.getYear());
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            debug = true;
            logger.info("Debug mode");
        }

        new Scraper();
        logger.info("Exiting...");
    }
    
}
