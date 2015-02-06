import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Helper {

    public static String getCurrentIP() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                new URL("http://wtfismyip.com/text").openConnection().getInputStream()
            ));

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