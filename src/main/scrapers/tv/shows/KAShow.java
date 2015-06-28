package main.scrapers.tv.shows;

import main.scrapers.tv.TVHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class KAShow extends Show {

    public KAShow(String t, String u, String eu, int s, int e, int h, int r) {
        super(t, u, eu, s, e, h,r);
    }

    public void parse() {
        Document doc = TVHelper.retrievePage(url);

        if (doc == null) return;

        Element main = doc.select(".mainpart").first().select("table").first().select("tr").first();
        int season = Integer.parseInt(main.select("h3").get(0).text().split(" ")[1].replace(",", ""));
        List<Element> eps = main.select("div.versionsFolded").stream().collect(Collectors.toList());

        for (Element e : eps) {
            String stringID = e.select(".infoListCut").first().attr("onclick").replace("showEpisodeInfo(this,", "")
                    .replace(");", "").replace("'", "").trim();
            Elements span = e.select(".infoListCut").first().select("span");
            int epNum = Integer.parseInt(span.get(0).text().split(" ")[1]);

            if (span.size() <= 2) {
                if (e.text().startsWith("Episode 01")) {
                    season--;
                }

                continue;
            }

            try {
                long timestamp = new SimpleDateFormat("EEEE, MMM dd yyyy").parse(span.get(2).text()).getTime();

                if (!stringID.equals("") && timestamp < System.currentTimeMillis()) {
                    episodes.add(new KAEpisode(episodeUrl + stringID, season, epNum));
                }

                if (e.text().startsWith("Episode 01")) {
                    season--;
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }


}