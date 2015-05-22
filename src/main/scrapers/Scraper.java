package main.scrapers;

import main.scrapers.movie.MovieScraper;
import main.scrapers.tv.TVScraper;

import java.util.logging.Logger;

public class Scraper {

    public static boolean debug = false;
    public static Logger logger = Logger.getLogger(Scraper.class.getName());
    
    public static void main(String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "tv":
                    logger.info("Running tv");
                    new TVScraper();
                    break;
                case "movie":
                    logger.info("Running movie");
                    new MovieScraper();
                    break;
                default:
                    logger.info("Must specify [tv, movie]");
                    break;
            }
        } else {
            logger.info("Need a scraper to run. [tv, movie]");
        }
    }
    
}
