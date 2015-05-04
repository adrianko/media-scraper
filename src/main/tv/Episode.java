package main.tv;

import java.util.LinkedList;
import java.util.List;

public abstract class Episode {

    protected String url;
    protected int season;
    protected int episode;
    protected List<DownloadOption> options = new LinkedList<>();

    public Episode(String u) {
        this(u, 0, 0);
    }

    public Episode(String u, int s, int e) {
        url = u;
        season = s;
        episode = e;
    }

    public abstract void parse();

    public List<DownloadOption> getOptions() {
        return options;
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
    
}