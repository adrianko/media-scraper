import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Episode {

    private String url;
    private int season;
    private int episode;
    private List<DownloadOption> options = new LinkedList<>();

    public Episode(String u) {
        url = u;
        parse();
    }

    public void parse() {
        try {
            Document doc = Jsoup.connect(url).get();

            for (Element e : doc.select("tr.odd, tr.even")) {
                String name = e.select("td").first().select(".torrentname").first().select("a.cellMainLink")
                    .first().text();
                String magnet = e.select("td").first().select(".iaconbox").first().select("a.imagnet").first()
                    .attr("href").split("&")[0];
                String size = e.select("td.nobr").first().text().replaceAll("\\s+", "");

                options.add(new DownloadOption(name, magnet, size));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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