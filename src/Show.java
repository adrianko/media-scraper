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

    public Show(String t, String u, String eu) {
        title = t;
        url = u;
        episodeUrl = eu;
    }

    public abstract void parse();

    public String getTitle() {
        return title;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
}
