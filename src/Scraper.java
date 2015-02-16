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

        try {
            Helper.loadSettings();
    
            if (settings.get("ip").equals(Helper.getCurrentIP())) {
                System.out.println("Wrong IP, exiting...");
                System.exit(0);
            }
            
            ResultSet rs = DB.getShows();
    
            while (rs.next()) {
                Show show = new KAShow(
                    rs.getString("title"), 
                    settings.get("ka_base") + rs.getString("ka_url"), 
                    settings.get("ka_ep"), 
                    rs.getInt("season"), 
                    rs.getInt("episode"), 
                    rs.getInt("hd"), 
                    rs.getInt("runtime")
                );
    
                for (Episode ep : show.getEpisodes()) {
                    KAEpisode e = (KAEpisode) ep;
                    boolean found = false;
                    
                    for (DownloadOption t : e.getOptions()) {
                        if (Helper.validateOption(t, e, show)) {
                            System.out.println("Found: " + t.getName() + " " + t.getMagnet());
                            Downloader.enqueue(t.getMagnet());
                            DB.bump(t.getName());
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        System.out.println("None found for: " + show.getTitle() +
                                " S" + (rs.getInt("season") < 10 ? "0" : "") + rs.getInt("season") +
                                "E" + (rs.getInt("episode") < 10 ? "0" : "") + rs.getInt("episode"));
                    }
                }
            }
    
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.close();
        }
    }

}