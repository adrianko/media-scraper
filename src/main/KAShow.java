package main;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class KAShow extends Show {

    public KAShow(String t, String u, String eu, int s, int e, int h, int r) {
        super(t, u, eu, s, e, h,r);
    }

    public void parse() {
        Document doc = Helper.retrievePage(url);

        if (doc != null) {
            Element main = doc.select(".mainpart").first().select("table").first().select("tr").first();
            int season = Integer.parseInt(main.select("h3").get(0).text().split(" ")[1].replace(",", ""));

            for (Element e : main.select("div.versionsFolded")) {
                String stringID = e.select(".infoListCut").first().attr("onclick").replace("showEpisodeInfo(this,", "")
                    .replace(");", "").replace("'", "").trim();
                Elements span = e.select(".infoListCut").first().select("span");
                int epNum = Integer.parseInt(span.get(0).text().split(" ")[1]);

                if (span.size() > 2) {
                    try {
                        long timestamp = new SimpleDateFormat("EEEE, MMM dd yyyy").parse(span.get(2).text()).getTime();

                        if (!stringID.equals("") && timestamp < System.currentTimeMillis()) {
                            /*
                            KAEpisode ep = new KAEpisode(episodeUrl + stringID);
                            ep.parse();

                            if (ep.getOptions().size() > 0) {
                                ep.setSeason(season);
                                ep.setEpisode(epNum);
                                episodes.add(ep);
                            }
                            */
                            episodes.add(new KAEpisode(episodeUrl + stringID));
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.out.println(episodes);
        }
    }

}