import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Double quality download
 * TODO add secondary source
 * TODO add connection check
 * TODO SD precedence HD
 * TODO 720 categorization
 */
public class Scraper {

    public static Map<String, String> settings = new HashMap<>();
    public static Map<Integer, HashMap<String, Long>> expectedFileSize = new HashMap<>();

    public static void main(String[] args) {
        if (!System.getProperty("os.name").contains("Windows")) {
            System.out.println("This application is not designed to run on any operating system other than Windows. Sorry.");
            System.exit(0);
        }

        try (Statement s = DB.get().createStatement()) {
            Helper.loadSettings();

            if (settings.get("ip").equals(Helper.getCurrentIP())) {
                System.out.println("Wrong IP, exiting...");
                System.exit(0);
            }

            PreparedStatement update = DB.get().prepareStatement("UPDATE shows SET episode = ? WHERE title = ?");
            ResultSet rs = s.executeQuery("SELECT * FROM shows");

            while (rs.next()) {
                Show show = new KAShow(rs.getString("title"), settings.get("ka_base") + rs.getString("ka_url"), settings.get("ka_ep"));

                for (KAEpisode e : show.getEpisodes()) {
                    boolean found = false;
                    String quality = (rs.getInt("hd") == 1 ? "1080p" : "HDTV");
                    
                    for (DownloadOption t : e.getOptions()) {
                        if (!t.getName().contains(quality)) continue;

                        if (t.getName().contains("ReEnc")) continue;

                        if (e.getEpisode() != rs.getInt("episode")) continue;

                        if (t.getName().contains("720p")) continue;

                        if (t.getByteSize() < expectedFileSize.get(rs.getInt("runtime")).get(quality + "_min")) continue;

                        if (t.getByteSize() > expectedFileSize.get(rs.getInt("runtime")).get(quality + "_max")) continue;

                        System.out.println("Found: " + t.getName() + " " + t.getMagnet());
                        Downloader.enqueue(t.getMagnet());

                        update.setInt(1, rs.getInt("episode") + 1);
                        update.setString(2, rs.getString("title"));
                        update.executeUpdate();
                        found = true;
                        break;
                    }
                    
                    if (!found) {
                        System.out.println("None found for: " + show.getTitle() +
                                " S" + (rs.getInt("season") < 10 ? "0" : "") + rs.getInt("season") +
                                "E" + (rs.getInt("episode") < 10 ? "0" : "") + rs.getInt("episode"));
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