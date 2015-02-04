import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @todo File size checking
 * @todo Double quality download
 * @todo add secondary source
 * @todo add connection check
 */
public class Scraper {

    public static Map<String, String> gSettings = new HashMap<>();

    public static void main(String[] args) {
        Statement s = null;

        try {
            s = Base.get().createStatement();

            ResultSet settings = s.executeQuery("SELECT * FROM settings");

            while (settings.next()) {
                gSettings.put(settings.getString("property"), settings.getString("value"));
            }

            PreparedStatement update = Base.get().prepareStatement("UPDATE shows SET episode = ? WHERE title = ?");
            ResultSet rs = s.executeQuery("SELECT * FROM shows");

            while (rs.next()) {
                KAShow show = new KAShow(rs.getString("title"), gSettings.get("ka_base") + rs.getString("ka_url"), gSettings.get("ka_ep"));

                for (KAEpisode e : show.getEpisodes()) {
                    for (DownloadOption t : e.getOptions()) {
                        String quality = (rs.getInt("hd") == 1 ? "1080p" : "HDTV");

                        if (t.getName().contains(quality) && !t.getName().contains("ReEnc") && e.getEpisode() == rs.getInt("episode")) {
                            System.out.println(t.getName() + " " + t.getMagnet());
                            Runtime.getRuntime().exec("cmd /c start " + t.getMagnet());

                            update.setInt(1, rs.getInt("episode") + 1);
                            update.setString(2, rs.getString("title"));
                            update.executeUpdate();
                            break;
                        }
                    }
                }
            }

            rs.close();
            update.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Base.close();
        }
    }

}