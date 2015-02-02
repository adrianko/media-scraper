import java.util.LinkedList;
import java.util.List;

public abstract class Show {

    protected String title;
    protected List<KAEpisode> episodes = new LinkedList<>();
    protected String url;

    public abstract void parse();

    public String getTitle() {
        return title;
    }

    public List<KAEpisode> getEpisodes() {
        return episodes;
    }
}
