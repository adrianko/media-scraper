import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Helper {

    public static String getCurrentIP() {
        try {
            URL u = new URL("http://wtfismyip.com/text");
            URLConnection conn = u.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String contents = "";
            String line;

            while ((line = br.readLine()) != null) {
                contents += line;
            }

            br.close();

            return contents.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}