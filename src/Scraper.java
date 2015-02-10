import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO File size checking
 * TODO Double quality download
 * TODO add secondary source
 * TODO add connection check
 * TODO SD precedence HD
 * TODO 720 categorization
 */
public class Scraper {

    public static Map<String, String> settings = new HashMap<>();

    public static void main(String[] args) {
        try (Statement s = DB.get().createStatement()) {
            Helper.loadSettings();

            if (settings.get("ip").equals(Helper.getCurrentIP())) {
                System.out.println("Wrong IP, exiting...");
                System.exit(0);
            }

            PreparedStatement update = DB.get().prepareStatement("UPDATE shows SET episode = ? WHERE title = ?");
            ResultSet rs = s.executeQuery("SELECT * FROM shows");

            while (rs.next()) {
                KAShow show = new KAShow(rs.getString("title"), settings.get("ka_base") + rs.getString("ka_url"), settings.get("ka_ep"));

                for (KAEpisode e : show.getEpisodes()) {
                    for (DownloadOption t : e.getOptions()) {
                        String quality = (rs.getInt("hd") == 1 ? "1080p" : "HDTV");

                        if (t.getName().contains(quality) && !t.getName().contains("ReEnc") && e.getEpisode() == rs.getInt("episode")) {
                            System.out.println(t.getName() + " " + t.getMagnet());
                            Downloader.enqueue(t.getMagnet());

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
            DB.close();
        }
    }

}