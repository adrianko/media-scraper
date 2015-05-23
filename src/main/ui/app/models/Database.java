package main.ui.app.models;

import main.scrapers.tv.shows.Show;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Database {
    
    public List<Show> getTVShows() {
        return new LinkedList<>();
    }
    
    public List<Show> getMovies() {
        return new LinkedList<>();
    }
    
    public Map<String, String> getSettings(String category) {
        return new HashMap<>();
    }
    
    public Map<String, String> getGlobalSettings() {
        return new HashMap<>();
    }
    
}
