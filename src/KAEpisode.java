import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class KAEpisode extends Episode {

    public KAEpisode(String u) {
        super(u);
        parse();
    }

    public void parse() {
        try {
            Document doc = Jsoup.connect(url).timeout(30000).get();

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

}