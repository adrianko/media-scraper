package main.tv.shows;

import java.util.LinkedList;
import java.util.List;

public abstract class Show {

    protected String title;
    protected List<Episode> episodes = new LinkedList<>();
    protected String url;
    protected int season;
    protected int episode;
    protected int hd;
    protected int runtime;
    protected String episodeUrl;
    protected String quality;
    protected boolean found = false;

    public Show(String t, String u, String eu, int s, int e, int h, int r) {
        title = t;
        url = u;
        episodeUrl = eu;
        season = s;
        episode = e;
        hd = h;
        runtime = r;
        setQuality();
    }

    public abstract void parse();

    public String getTitle() {
        return title;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
    
    public int getSeason() {
        return season;
    }

    public void setSeason(int s) {
        season = s;
    }
    
    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int e) {
        episode = e;
    }
    
    public int getHD() {
        return hd;
    }
    
    public void setHD(int hd) {
        this.hd = hd;
        setQuality();
    }
    
    public int getRuntime() {
        return runtime;
    }
    
    public String getQuality() {
        return quality;
    }
    
    public void setQuality() {
        switch (getHD()) {
            case 1:
                quality = "1080p";
                break;
            case 2:
                quality = "720p";
                break;
            default:
                quality = "HDTV";
                break;
        }
    }

    public String toString() {
        return title + " S" + (season < 10 ? "0" : "") + season + "E" + (episode < 10 ? "0" : "") + episode;
    }

    public void setFound() {
        found = true;
    }

    public boolean getFound() {
        return found;
    }
    
}