package main.scrapers;

import main.scrapers.movie.MovieScraper;
import main.scrapers.tv.TVScraper;

import java.util.logging.Logger;

public class Scraper {

    public static boolean debug = false;
    public static Logger logger = Logger.getLogger(Scraper.class.getName());
    
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("tv")) {
                new TVScraper();
            } else if (args[0].equals("movie")) {
                new MovieScraper();
            }
        } else {
            System.out.println("Need a scraper to run. [tv, movie]");
        }
    }
    
}
