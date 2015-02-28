package main;

import org.jsoup.nodes.Document;

public class KAEpisode extends Episode {

    public KAEpisode(String u) {
        this(u, 0 , 0);
    }

    public KAEpisode(String u, int s, int e) {
        super(u, s, e);
    }

    public void parse() {
        Document doc = Helper.retrievePage(url);

        if (doc != null) {
            doc.select("tr.odd, tr.even").forEach(e -> {
                String name = e.select("td").first().select(".torrentname").first().select("a.cellMainLink").first().text();
                String magnet = e.select("td").first().select(".iaconbox").first().select("a.imagnet").first().attr("href").split("&")[0];
                String size = e.select("td.nobr").first().text().replaceAll("\\s+", "");

                options.add(new DownloadOption(name, magnet, size));
            });
        }
    }

}