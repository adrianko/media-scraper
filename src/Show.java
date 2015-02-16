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

    public Show(String t, String u, String eu, int s, int e, int h, int r) {
        title = t;
        url = u;
        episodeUrl = eu;
        season = s;
        episode = e;
        hd = h;
        runtime = r;
        quality = (getHD() == 1 ? "1080p" : "HDTV");
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
    
    public int getEpisode() {
        return episode;
    }
    
    public int getHD() {
        return hd;
    }
    
    public int getRuntime() {
        return runtime;
    }
    
    public String getQuality() {
        return quality;
    }
    
}