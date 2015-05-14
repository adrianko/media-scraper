package main.tv.shows;

import main.Helper;
import main.tv.TVHelper;
import org.jsoup.nodes.Document;

public class KAEpisode extends Episode {

    public KAEpisode(String u) {
        this(u, 0 , 0);
    }

    public KAEpisode(String u, int s, int e) {
        super(u, s, e);
    }

    public void parse() {
        Document doc = TVHelper.retrievePage(url);

        if (doc != null) {
            doc.select("tr.odd, tr.even").forEach(e -> options.add(new DownloadOption(Helper.retrieveTitle(e), Helper
                .retrieveMagnet(e), e.select("td.nobr").first().text().replaceAll("\\s+", ""))));
        }
    }

}