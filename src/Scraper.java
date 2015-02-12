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
    public static Map<String, HashMap<String, Long>> expectedFileSize;
    static {
        Map<String, HashMap<String, Long>> fs = new HashMap<>();
        fs.put("1080p", new HashMap<>());
        fs.get("1080p").put("min", 700000000L);
        fs.get("1080p").put("max", 2500000000L);
        fs.put("HDTV", new HashMap<>());
        fs.get("HDTV").put("min", 100000000L);
        fs.get("HDTV").put("min", 500000000L);
        expectedFileSize = fs;
    }

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

                        if (!t.getName().contains(quality)) continue;

                        if (t.getName().contains("ReEnc")) continue;

                        if (e.getEpisode() != rs.getInt("episode")) continue;

                        if (t.getName().contains("720p")) continue;

                        if (t.getByteSize() < expectedFileSize.get(quality).get("min")) continue;

                        if (t.getByteSize() > expectedFileSize.get(quality).get("max")) continue;

                        System.out.println(t.getName() + " " + t.getMagnet());
                        Downloader.enqueue(t.getMagnet());

                        update.setInt(1, rs.getInt("episode") + 1);
                        update.setString(2, rs.getString("title"));
                        update.executeUpdate();
                    }
                }
            }

            rs.close();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.close();
        }
    }

}