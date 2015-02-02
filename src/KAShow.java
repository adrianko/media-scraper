import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class KAShow extends Show {

    public KAShow(String t, String u) {
        title = t;
        url = u;
        parse();
    }

    public void parse() {
        try {
            Document doc = Jsoup.connect(url).timeout(30000).get();
            Element main = doc.select(".mainpart").first().select("table").first().select("tr").first();
            int season = Integer.parseInt(main.select("h3").get(0).text().split(" ")[1].replace(",", ""));

            for (Element e : main.select("div.versionsFolded")) {
                String stringID = e.select(".infoListCut").first().attr("onclick").replace("showEpisodeInfo(this,", "")
                    .replace(");", "").replace("'", "").trim();
                Elements span = e.select(".infoListCut").first().select("span");
                int epNum = Integer.parseInt(span.get(0).text().split(" ")[1]);

                if (span.size() > 2) {
                    long timestamp = new SimpleDateFormat("EEEE, MMM dd yyyy").parse(span.get(2).text()).getTime();

                    if (!stringID.equals("") && timestamp < System.currentTimeMillis()) {
                        System.out.println(title + " " + Scraper.doUrl + stringID);
                        KAEpisode ep = new KAEpisode(Scraper.doUrl + stringID);

                        if (ep.getOptions().size() > 0) {
                            ep.setSeason(season);
                            ep.setEpisode(epNum);
                            episodes.add(ep);
                            break;
                        }
                    }
                }
            }

        } catch (IOException |ParseException e) {
            e.printStackTrace();
        }
    }

}